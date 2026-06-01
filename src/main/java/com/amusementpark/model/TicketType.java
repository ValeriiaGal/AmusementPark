package com.amusementpark.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket_type")
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank
    @Column(unique = true, name = "type_name")
    private String typeName;

    @Column(name = "is_vip")
    private boolean isVip;

    @DecimalMin("0.0")
    @Column(name = "initial_price")
    private double initialPrice;

    // Qualified association: Zone has TicketTypes (many to many)
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "ticket_type_zone",
            joinColumns = @JoinColumn(name = "ticket_type_id"),
            inverseJoinColumns = @JoinColumn(name = "zone_id")
    )
    @MapKey(name = "name")
    private Map<String, Zone> zonesByName = new HashMap<>();


    // Basic association: TicketType - Tickets (1 to many)
    @Builder.Default
    @OneToMany(mappedBy = "ticketType", cascade = CascadeType.PERSIST)
    private Set<Ticket> tickets = new HashSet<>();


    public Set<Zone> getAccessibleZones() {
        return new HashSet<>(zonesByName.values());
    }

}
