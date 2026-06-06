package com.amusementpark.model;

import com.amusementpark.model.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank
    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Min(0)
    @Max(100)
    @Column(name = "personal_discount")
    private Integer personalDiscount;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();

    public double getTotalPrice() {
        double total = 0;
        for (Ticket ticket : tickets) {
            double ticketPrice = ticket.getTicketType().getInitialPrice() * ticket.getQuantity();
            total += ticketPrice;
        }

        if (promotion != null && promotion.isActive(java.time.LocalDate.now())) {
            total = total * (1 - promotion.getPromotionPercentage() / 100.0);
        }

        if (personalDiscount != null) {
            total = total * (1 - personalDiscount / 100.0);
        }

        return Math.round(total * 100.0) / 100.0;
    }

    public void fillOrderDetails() {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order must be in CREATED state to fill details.");
        }
        this.status = OrderStatus.IN_PROGRESS;
    }

    public void pay(String paymentMethod) {
        if (this.status != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Order must be in IN_PROGRESS state to pay.");
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method is required.");
        }
        this.paymentMethod = paymentMethod;
        this.status = OrderStatus.PAID;
    }

    public void confirmOrder() {
        if (this.status != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be in PAID state to confirm.");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Completed orders cannot be cancelled.");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
