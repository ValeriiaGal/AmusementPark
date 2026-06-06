package com.amusementpark.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @Min(0)
    @Max(100)
    @Column(name = "promotion_percentage")
    private int promotionPercentage;

    @Builder.Default
    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

    public boolean isActive(LocalDate date) {
        return date != null && !date.isBefore(startDate) && !date.isAfter(endDate);
    }




}
