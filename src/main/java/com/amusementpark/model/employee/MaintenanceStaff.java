package com.amusementpark.model.employee;

import com.amusementpark.model.MalfunctionReport;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "maintenance_staff")
public class MaintenanceStaff extends Employee{

    @NotBlank
    private String specialization;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "maintenance_certifications", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "certification", nullable = false)
    private Set<@NotBlank String> certifications = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "maintenanceStaff", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<MalfunctionReport> reports = new HashSet<>();


}
