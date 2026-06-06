package com.amusementpark.service;

import com.amusementpark.model.*;
import com.amusementpark.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZoneService {

    private final ZoneRepository zoneRepository;


    public Zone getZoneWithAttractionsById(Long id) {
        return zoneRepository.findByIdWithAttractions(id)
                .orElseThrow(() -> new RuntimeException("Zone not found with id: " + id));

    }


}
