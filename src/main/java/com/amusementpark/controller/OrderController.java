package com.amusementpark.controller;

import com.amusementpark.dto.CartItem;
import com.amusementpark.model.Customer;
import com.amusementpark.model.Order;
import com.amusementpark.model.Promotion;
import com.amusementpark.model.TicketType;
import com.amusementpark.model.Zone;
import com.amusementpark.service.CartService;
import com.amusementpark.service.CheckoutService;
import com.amusementpark.service.OrderService;
import com.amusementpark.service.ZoneService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ZoneService zoneService;
    private final CartService cartService;
    private final CheckoutService checkoutService;

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void clearCheckoutSession(HttpSession session) {
        session.removeAttribute("cart");
        session.removeAttribute("promotionId");
        session.removeAttribute("personalDiscount");
        session.removeAttribute("paymentMethod");
    }

    private boolean isCartEmpty(HttpSession session) {
        return getCart(session).isEmpty();
    }

    private String redirectToTicketsWhenCartIsEmpty(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isCartEmpty(session)) {
            return null;
        }

        redirectAttributes.addFlashAttribute("error", "Your cart is empty");
        return "redirect:/tickets";
    }

    @GetMapping("/tickets")
    public String viewTicketTypes(
            @RequestParam(required = false) Long ticketTypeId,
            @RequestParam(required = false) Long zoneId,
            Model model,
            HttpSession session) {

        List<TicketType> ticketTypes = orderService.getAllTicketTypes();
        model.addAttribute("ticketTypes", ticketTypes);
        model.addAttribute("cartSize", getCart(session).size());

        if (ticketTypeId != null) {
            TicketType selectedTicketType = orderService.getTicketTypeById(ticketTypeId);
            Set<Zone> zones = selectedTicketType.getAccessibleZones();
            model.addAttribute("selectedTicketType", selectedTicketType);
            model.addAttribute("zones", zones);

            if (zoneId != null) {
                Zone selectedZone = zoneService.getZoneWithAttractionsById(zoneId);
                model.addAttribute("selectedZone", selectedZone);
                model.addAttribute("attractions", selectedZone.getAttractions());
            }
        }

        return "tickets";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long ticketTypeId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        TicketType ticketType = cartService.addTicket(getCart(session), ticketTypeId);
        redirectAttributes.addFlashAttribute("success", ticketType.getTypeName() + " added to cart");
        return "redirect:/tickets";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("total", cartService.calculateCartTotal(cart));
        model.addAttribute("step", 1);
        return "cart";
    }

    @GetMapping("/orders")
    public String viewPersistedOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrdersWithTickets());
        return "orders";
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long ticketTypeId,
                                 @RequestParam int quantity,
                                 HttpSession session) {
        cartService.updateQuantity(getCart(session), ticketTypeId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long ticketTypeId, HttpSession session) {
        cartService.removeTicket(getCart(session), ticketTypeId);
        return "redirect:/cart";
    }

    @GetMapping("/checkout/summary")
    public String orderSummary(Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String emptyCartRedirect = redirectToTicketsWhenCartIsEmpty(session, redirectAttributes);
        if (emptyCartRedirect != null) {
            return emptyCartRedirect;
        }

        Map<Long, CartItem> cart = getCart(session);

        model.addAttribute("itemsWithZones", checkoutService.buildSummaryItems(cart));
        model.addAttribute("total", cartService.calculateCartTotal(cart));
        model.addAttribute("step", 2);
        return "checkout-summary";
    }

    @GetMapping("/checkout/promotions")
    public String selectPromotion(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String emptyCartRedirect = redirectToTicketsWhenCartIsEmpty(session, redirectAttributes);
        if (emptyCartRedirect != null) {
            return emptyCartRedirect;
        }

        model.addAttribute("promotions", orderService.getActivePromotions());
        model.addAttribute("selectedPromotionId", session.getAttribute("promotionId"));
        model.addAttribute("step", 3);
        return "checkout-promotions";
    }

    @PostMapping("/checkout/promotions")
    public String applyPromotion(@RequestParam(required = false) Long promotionId,
                                 HttpSession session) {
        session.setAttribute("promotionId", promotionId);
        return "redirect:/checkout/discount";
    }

    @GetMapping("/checkout/discount")
    public String enterDiscount(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String emptyCartRedirect = redirectToTicketsWhenCartIsEmpty(session, redirectAttributes);
        if (emptyCartRedirect != null) {
            return emptyCartRedirect;
        }

        model.addAttribute("personalDiscount", session.getAttribute("personalDiscount"));
        model.addAttribute("step", 4);
        return "checkout-discount";
    }

    @PostMapping("/checkout/discount")
    public String applyDiscount(@RequestParam(required = false) String discountCode,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (discountCode != null && !discountCode.isBlank()) {
            Integer discount = orderService.validateDiscountCode(discountCode);
            if (discount == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid discount code.");
                return "redirect:/checkout/discount";
            }

            session.setAttribute("personalDiscount", discount);
            redirectAttributes.addFlashAttribute("success", "Valid! " + discount + "% discount applied.");
        }

        return "redirect:/checkout/final-summary";
    }

    @PostMapping("/checkout/skip-discount")
    public String skipDiscount() {
        return "redirect:/checkout/final-summary";
    }

    @GetMapping("/checkout/final-summary")
    public String finalSummary(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String emptyCartRedirect = redirectToTicketsWhenCartIsEmpty(session, redirectAttributes);
        if (emptyCartRedirect != null) {
            return emptyCartRedirect;
        }

        Map<Long, CartItem> cart = getCart(session);
        Long promotionId = (Long) session.getAttribute("promotionId");
        Integer personalDiscount = (Integer) session.getAttribute("personalDiscount");
        Promotion promotion = orderService.getActivePromotionById(promotionId).orElse(null);

        Map<Long, Double> discountedUnitPrices = new HashMap<>();
        Map<Long, Double> discountedLineTotals = new HashMap<>();
        for (CartItem item : cart.values()) {
            double discountedUnitPrice = orderService.calculateDiscountedUnitPrice(
                    item.getUnitPrice(), promotionId, personalDiscount
            );
            discountedUnitPrices.put(item.getTicketTypeId(), discountedUnitPrice);
            discountedLineTotals.put(item.getTicketTypeId(), discountedUnitPrice * item.getQuantity());
        }

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("discountedUnitPrices", discountedUnitPrices);
        model.addAttribute("discountedLineTotals", discountedLineTotals);
        model.addAttribute("promotion", promotion);
        model.addAttribute("personalDiscount", personalDiscount);
        model.addAttribute("subtotal", cartService.calculateCartTotal(cart));
        model.addAttribute("total", orderService.calculateTotal(cart, promotionId, personalDiscount));
        model.addAttribute("step", 5);
        return "checkout-final";
    }

    @GetMapping("/checkout/payment")
    public String selectPayment(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String emptyCartRedirect = redirectToTicketsWhenCartIsEmpty(session, redirectAttributes);
        if (emptyCartRedirect != null) {
            return emptyCartRedirect;
        }

        Map<Long, CartItem> cart = getCart(session);
        Long promotionId = (Long) session.getAttribute("promotionId");
        Integer personalDiscount = (Integer) session.getAttribute("personalDiscount");

        model.addAttribute("total", orderService.calculateTotal(cart, promotionId, personalDiscount));
        model.addAttribute("step", 6);
        return "checkout-payment";
    }

    @PostMapping("/checkout/payment-method")
    public String choosePaymentMethod(@RequestParam String paymentMethod,
                                      HttpSession session) {
        session.setAttribute("paymentMethod", paymentMethod);
        return "redirect:/checkout/payment-details";
    }

    @GetMapping("/checkout/payment-details")
    public String paymentDetails(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String emptyCartRedirect = redirectToTicketsWhenCartIsEmpty(session, redirectAttributes);
        if (emptyCartRedirect != null) {
            return emptyCartRedirect;
        }

        String paymentMethod = (String) session.getAttribute("paymentMethod");
        if (paymentMethod == null) {
            return "redirect:/checkout/payment";
        }

        Map<Long, CartItem> cart = getCart(session);
        Long promotionId = (Long) session.getAttribute("promotionId");
        Integer personalDiscount = (Integer) session.getAttribute("personalDiscount");

        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("total", orderService.calculateTotal(cart, promotionId, personalDiscount));
        model.addAttribute("step", 7);
        return "checkout-payment-details";
    }

    @PostMapping("/checkout/pay")
    public String processPayment(@RequestParam Map<String, String> paymentDetails,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            String emptyCartRedirect = redirectToTicketsWhenCartIsEmpty(session, redirectAttributes);
            if (emptyCartRedirect != null) {
                return emptyCartRedirect;
            }

            String paymentMethod = (String) session.getAttribute("paymentMethod");
            if (paymentMethod == null) {
                return "redirect:/checkout/payment";
            }

            orderService.validatePaymentDetails(paymentMethod, paymentDetails);

            Map<Long, CartItem> cart = getCart(session);
            Long promotionId = (Long) session.getAttribute("promotionId");
            Integer personalDiscount = (Integer) session.getAttribute("personalDiscount");
            Customer customer = orderService.getDefaultCustomer();
            Order order = orderService.createOrder(customer.getId(), cart, promotionId, personalDiscount);
            Order completed = orderService.payOrder(order.getId(), paymentMethod);

            clearCheckoutSession(session);
            session.setAttribute("lastOrderId", completed.getId());

            return "redirect:/checkout/confirmation";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment failed: " + e.getMessage());
            return "redirect:/checkout/payment-details";
        }
    }

    @GetMapping("/checkout/confirmation")
    public String confirmation(Model model, HttpSession session) {
        Long orderId = (Long) session.getAttribute("lastOrderId");
        if (orderId == null) {
            return "redirect:/tickets";
        }

        Order order = orderService.getOrderWithTickets(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found"));

        model.addAttribute("order", order);
        model.addAttribute("step", 8);
        return "checkout-confirmation";
    }
}
