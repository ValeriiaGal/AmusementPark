package com.amusementpark.service;

import com.amusementpark.dto.CartItem;
import com.amusementpark.model.TicketType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final OrderService orderService;

    public TicketType addTicket(Map<Long, CartItem> cart, Long ticketTypeId) {
        TicketType ticketType = orderService.getTicketTypeById(ticketTypeId);

        if (cart.containsKey(ticketTypeId)) {
            CartItem existingItem = cart.get(ticketTypeId);
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            CartItem item = new CartItem(
                    ticketType.getId(),
                    ticketType.getTypeName(),
                    ticketType.getInitialPrice(),
                    1,
                    ticketType.isVip(),
                    ticketType.getAccessibleZones().size(),
                    LocalDate.now(),
                    LocalDate.now()
            );
            cart.put(ticketTypeId, item);
        }

        return ticketType;
    }

    public void updateQuantity(Map<Long, CartItem> cart, Long ticketTypeId, int quantity) {
        if (quantity <= 0) {
            cart.remove(ticketTypeId);
            return;
        }

        CartItem item = cart.get(ticketTypeId);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void removeTicket(Map<Long, CartItem> cart, Long ticketTypeId) {
        cart.remove(ticketTypeId);
    }

    public double calculateCartTotal(Map<Long, CartItem> cart) {
        return cart.values().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }
}
