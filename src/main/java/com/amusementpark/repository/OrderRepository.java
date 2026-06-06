package com.amusementpark.repository;

import com.amusementpark.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.tickets t LEFT JOIN FETCH t.ticketType tt LEFT JOIN FETCH tt.zonesByName WHERE o.id = :id")
    Optional<Order> findByIdWithTicketsAndTypes(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.tickets t LEFT JOIN FETCH t.ticketType tt LEFT JOIN FETCH tt.zonesByName WHERE o.customer.id = :customerId")
    List<Order> findByCustomerIdWithTicketsAndTypes(@Param("customerId") Long customerId);
}
