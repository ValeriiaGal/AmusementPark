package com.amusementpark.model.attraction;

import com.amusementpark.model.Zone;
import com.amusementpark.model.enums.AttractionStatus;
import com.amusementpark.model.enums.Intensity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "attraction")
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank(message = "Attraction name is required")
    private String name;

    @Column(name = "does_vip_pass_work")
    private boolean doesVipPassWork;

    @Column(name = "attraction_height")
    @Min(value = 0, message = "Height must be non-negative")
    @Positive
    private int attractionHeight;

    @Column(name = "max_seats")
    @Min(value = 1, message = "Must have at least one seat")
    private int maxSeats;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AttractionStatus status = AttractionStatus.ACTIVE;

    // multi aspect inheritance as enum
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private Intensity intensity;

    // Extreme attributes (optional)
    @Column(name = "safety_restrictions")
    private String safetyRestrictions;

    @Column(name = "minimum_height")
    private Integer minHeight;

    // Light attributes (optional)
    @Column(name = "has_animators")
    private Boolean hasAnimators;

    @Column(name = "is_parent_supervision_required")
    private Boolean IsParentSupervisionRequired;

    // Attraction - zone relationship
    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    // Attraction - Malfunction report relationship (composition)
    @Builder.Default
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MalfunctionReport> malfunctionReports = new HashSet<>();

    // Attraction - Shift - Employee (association table)
    @Builder.Default
    @OneToMany(mappedBy = "attraction")
    private Set<Shift> shifts = new HashSet<>();

    public void fixAttraction() {
        this.status = AttractionStatus.ACTIVE;
    }

    public void reportMalfunction() {
        this.status = AttractionStatus.UNDER_MAINTENANCE;
    }

    public void closeAttraction() {
        this.status = AttractionStatus.CLOSED;
    }




}
