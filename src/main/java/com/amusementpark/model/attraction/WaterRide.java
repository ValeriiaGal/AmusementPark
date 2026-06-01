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
public class WaterRide extends Attraction {

    @Min(value = 0, message = "Water depth must be non-negative")
    private double waterDepth;

    @Min(value = 20, message = "Water depth must be at least 20C for safety")
    private double waterTemperature;
}
