package com.amusementpark.dto;

import com.amusementpark.model.Zone;
import com.amusementpark.model.attraction.Attraction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class CheckoutZoneDetails {

    private Zone zone;
    private Set<Attraction> attractions;
}
