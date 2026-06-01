package com.amusementpark.model;

import com.amusementpark.model.enums.ShopType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank
    private String name;

    // enum shop type
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shop_type")
    private ShopType type;

    @NotBlank
    @Column(name = "location")
    private String location;

    // Basic association: Shop - Zone (many to one)
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

}
