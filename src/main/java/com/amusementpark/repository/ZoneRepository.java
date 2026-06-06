package com.amusementpark.repository;

import com.amusementpark.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    @Query("SELECT z FROM Zone z LEFT JOIN FETCH z.attractions WHERE z.id = :id")
    Optional<Zone> findByIdWithAttractions(@Param("id") Long id);
}
