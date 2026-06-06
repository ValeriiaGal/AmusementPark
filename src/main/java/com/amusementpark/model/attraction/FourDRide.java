package com.amusementpark.model.attraction;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FourDRide extends Attraction{

    @Min(value = 0, message = "Show duration must be non-negative")
    private double showDuration;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "four_d_ride_effects", joinColumns = @JoinColumn(name = "ride_id"))
    @Column(name = "effect_type")
    @Size(min = 1, message = "At least one effect type is required")
    private Set<@NotBlank String> effectTypes = new HashSet<>();

}
