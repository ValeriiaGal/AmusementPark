package com.amusementpark.model.attraction;

import com.amusementpark.model.MalfunctionReport;
import com.amusementpark.model.Shift;
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
    @Min(value = 1, message = "Height must be non-negative")
    private int attractionHeight;

    @Column(name = "max_seats")
    @Min(value = 1, message = "Must have at least one seat")
    private int maxSeats;

    @Builder.Default
    @NotNull
    @Enumerated(EnumType.STRING)
    private AttractionStatus status = AttractionStatus.ACTIVE;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private Intensity intensity;

    @Column(name = "safety_restrictions")
    private String safetyRestrictions;

    @Column(name = "minimum_height")
    private Integer minHeight;

    @Column(name = "has_animators")
    private Boolean hasAnimators;

    @Column(name = "is_parent_supervision_required")
    private Boolean isParentSupervisionRequired;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Builder.Default
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MalfunctionReport> malfunctionReports = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "attraction", fetch = FetchType.LAZY)
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

    @AssertTrue(message = "Intensity-specific attributes are not valid")
    public boolean isIntensityConfigurationValid() {
        if (intensity == null) {
            return true;
        }
        if (intensity == Intensity.EXTREME) {
            return minHeight != null && minHeight > 0;
        }
        return hasAnimators != null && isParentSupervisionRequired != null;
    }
}
