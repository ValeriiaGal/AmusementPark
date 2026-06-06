package com.amusementpark.service;

import com.amusementpark.dto.CartItem;
import com.amusementpark.model.*;
import com.amusementpark.model.enums.OrderStatus;
import com.amusementpark.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final PromotionRepository promotionRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAllWithZones();

    }

    @Transactional(readOnly = true)
    public TicketType getTicketTypeById(Long id) {
        return ticketTypeRepository.findByIdWithZones(id)
                .orElseThrow(() -> new RuntimeException("Ticket type not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDate.now());

    }

    @Transactional(readOnly = true)
    public Optional<Promotion> getActivePromotionById(Long promotionId) {
        if (promotionId == null) {
            return Optional.empty();
        }

        return promotionRepository.findById(promotionId)
                .filter(promotion -> promotion.isActive(LocalDate.now()));
    }


    @Transactional(readOnly = true)
    public Optional<Order> getOrderWithTickets(Long orderId) {
        return orderRepository.findByIdWithTicketsAndTypes(orderId);

    }


    @Transactional(readOnly = true)
    public Customer getDefaultCustomer() {
        return customerRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No customers found in the system"));

    }

    @Transactional(readOnly = true)
    public Integer validateDiscountCode(String code) {
        Map<String, Integer> validCodes = Map.of(
                "ADVENTURE10", 10,
                "PARK20", 20,
                "SUMMER15", 12
        );
        return validCodes.get(code.toUpperCase());
    }

    public void validatePaymentDetails(String paymentMethod, Map<String, String> paymentDetails) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method is required.");
        }

        switch (paymentMethod) {
            case "Credit Card" -> requirePaymentFields(paymentDetails,
                    "cardholderName", "cardNumber", "expiryDate", "cvv");
            case "BLIK" -> requirePaymentFields(paymentDetails, "blikCode");
            case "Bank Transfer" -> requirePaymentFields(paymentDetails, "accountHolder", "transferEmail");
            case "Google Pay" -> requirePaymentFields(paymentDetails, "googlePayEmail");
            default -> throw new IllegalArgumentException("Unsupported payment method.");
        }
    }

    private void requirePaymentFields(Map<String, String> paymentDetails, String... fieldNames) {
        for (String fieldName : fieldNames) {
            String value = paymentDetails.get(fieldName);
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("Missing required payment details.");
            }
        }
    }

    @Transactional
    public Order createOrder(Long customerId, Map<Long, CartItem> cartItems,
                             Long promotionId, Integer personalDiscount) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        Promotion promotion = getActivePromotionById(promotionId).orElse(null);

        Order order = Order.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(OrderStatus.CREATED)
                .customer(customer)
                .promotion(promotion)
                .personalDiscount(personalDiscount)
                .build();

        order.fillOrderDetails();
        orderRepository.save(order);

        for (CartItem item : cartItems.values()) {
            TicketType ticketType = ticketTypeRepository.findById(item.getTicketTypeId())
                    .orElseThrow(() -> new RuntimeException("Ticket type not found with id: " + item.getTicketTypeId()));

            Ticket ticket = Ticket.builder()
                    .startDate(item.getStartDate())
                    .endDate(item.getEndDate())
                    .quantity(item.getQuantity())
                    .ticketType(ticketType)
                    .order(order)
                    .build();

            ticketRepository.save(ticket);
            order.getTickets().add(ticket);
        }

        return order;
    }

    @Transactional
    public Order payOrder(Long orderId, String paymentMethod) {
        Order order = orderRepository.findByIdWithTicketsAndTypes(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.pay(paymentMethod);
        order.confirmOrder();
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public double calculateTotal(Map<Long, CartItem> cartItems, Long promotionId, Integer personalDiscount) {
        double total = 0;
        for (CartItem item : cartItems.values()) {
            TicketType tt = ticketTypeRepository.findById(item.getTicketTypeId()).orElse(null);
            if (tt != null) {
                total += tt.getInitialPrice() * item.getQuantity();
            }
        }


        if (promotionId != null) {
            Promotion promo = promotionRepository.findById(promotionId).orElse(null);
            if (promo != null && promo.isActive(LocalDate.now())) {
                total = total * (1 - promo.getPromotionPercentage() / 100.0);
            }
        }

        if (personalDiscount != null && personalDiscount > 0) {
            total = total * (1 - personalDiscount / 100.0);
        }

        return Math.round(total * 100.0) / 100.0;
    }


}
