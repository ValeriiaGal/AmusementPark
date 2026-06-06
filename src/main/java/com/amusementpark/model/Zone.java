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

    @Builder.Default
    @OneToMany(mappedBy = "zone", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Attraction> attractions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "zone", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Shop> shops = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "zonesByName", fetch = FetchType.LAZY)
    private Set<TicketType> acceptedTicketTypes = new HashSet<>();

    @AssertTrue(message = "Closing time must be after opening time")
    public boolean isOpeningHoursValid() {
        return openingTime == null || closingTime == null || closingTime.isAfter(openingTime);
    }

}
