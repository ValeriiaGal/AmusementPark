package com.amusementpark.model;

import com.amusementpark.model.embeddable.ContactInfo;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Valid
    @NotNull
    @Embedded
    private ContactInfo contactInfo;

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

}
