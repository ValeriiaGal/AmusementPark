package com.amusementpark.repository;

import com.amusementpark.model.MalfunctionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MalfunctionReportRepository extends JpaRepository<MalfunctionReport, Long> {

}
