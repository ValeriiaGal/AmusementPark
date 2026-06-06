package com.amusementpark.dto;

import com.amusementpark.model.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class CheckoutSummaryItem {

    private CartItem item;
    private Set<Zone> zones;
    private List<CheckoutZoneDetails> zonesWithAttractions;
}
