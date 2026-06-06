package com.amusementpark.repository;

import com.amusementpark.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    @Query("SELECT DISTINCT tt FROM TicketType tt LEFT JOIN FETCH tt.zonesByName")
    List<TicketType> findAllWithZones();

    @Query("SELECT tt FROM TicketType tt LEFT JOIN FETCH tt.zonesByName WHERE tt.id = :id")
    Optional<TicketType> findByIdWithZones(@Param("id") Long id);
}
