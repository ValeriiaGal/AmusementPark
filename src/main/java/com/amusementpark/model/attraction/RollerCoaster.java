package com.amusementpark.model.attraction;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RollerCoaster extends Attraction{

    @Min(value = 0, message = "Track length must be non-negative")
    private double trackLength;

    @Min(value = 0, message = "Max speed must be non-negative")
    private double maxSpeed;

    @Min(value = 0, message = "Number of loops must be non-negative")
    private int numberOfLoops;
}
