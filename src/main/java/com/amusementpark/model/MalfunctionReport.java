package com.amusementpark.model;

import com.amusementpark.model.attraction.Attraction;
import com.amusementpark.model.employee.MaintenanceStaff;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "malfunction_report")
public class MalfunctionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotNull
    @Column(name = "date_performed")
    private LocalDate datePerformed;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_id", nullable = false, updatable = false)
    private Attraction attraction;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_staff_id", nullable = false)
    private MaintenanceStaff maintenanceStaff;
}
