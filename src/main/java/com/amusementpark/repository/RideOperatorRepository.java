package com.amusementpark.repository;

import com.amusementpark.model.employee.RideOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideOperatorRepository extends JpaRepository<RideOperator, Long> {

}
