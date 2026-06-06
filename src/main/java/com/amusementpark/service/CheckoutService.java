package com.amusementpark.service;

import com.amusementpark.dto.CartItem;
import com.amusementpark.dto.CheckoutSummaryItem;
import com.amusementpark.dto.CheckoutZoneDetails;
import com.amusementpark.model.TicketType;
import com.amusementpark.model.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final OrderService orderService;
    private final ZoneService zoneService;

    public List<CheckoutSummaryItem> buildSummaryItems(Map<Long, CartItem> cart) {
        List<CheckoutSummaryItem> summaryItems = new ArrayList<>();

        for (CartItem item : cart.values()) {
            TicketType ticketType = orderService.getTicketTypeById(item.getTicketTypeId());
            Set<Zone> zones = ticketType.getAccessibleZones();
            List<CheckoutZoneDetails> zonesWithAttractions = new ArrayList<>();

            for (Zone zone : zones) {
                Zone zoneWithAttractions = zoneService.getZoneWithAttractionsById(zone.getId());
                zonesWithAttractions.add(new CheckoutZoneDetails(
                        zoneWithAttractions,
                        zoneWithAttractions.getAttractions()
                ));
            }

            summaryItems.add(new CheckoutSummaryItem(item, zones, zonesWithAttractions));
        }

        return summaryItems;
    }
}
