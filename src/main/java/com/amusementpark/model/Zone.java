package com.amusementpark.model;

import com.amusementpark.model.attraction.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "zone")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    private String theme;

    @NotNull
    @Column(name = "opening_time")
    private LocalTime openingTime;

    @NotNull
    @Column(name = "closing_time")
    private LocalTime closingTime;

    // basic association: Zone has Attractions (1 to many)
    @Builder.Default
    @OneToMany(mappedBy = "zone", cascade = CascadeType.PERSIST)
    private Set<Attraction> attractions = new HashSet<>();

    // basic association: Zone has Shops (1 to many)
    @Builder.Default
    @OneToMany(mappedBy = "zone", cascade = CascadeType.PERSIST)
    private Set<Shop> shops = new HashSet<>();

    // Qualified association: Zone has TicketTypes (many to many)
    @Builder.Default
    @ManyToMany(mappedBy = "zonesByName")
    private Set<TicketType> acceptedTicketTypes = new HashSet<>();

    @AssertTrue(message = "Closing time must be after opening time")
    public boolean isOpeningHoursValid() {
        return openingTime == null || closingTime == null || closingTime.isAfter(openingTime);
    }

}
