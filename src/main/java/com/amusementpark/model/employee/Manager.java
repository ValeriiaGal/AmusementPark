package com.amusementpark.model.employee;

import com.amusementpark.model.Shift;
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
@Table(name = "manager")
public class Manager extends Employee{

    public static final int MIN_YEARS_OF_EXPERIENCE = 3;

    @Builder.Default
    @OneToMany(mappedBy = "assignedByManager", fetch = FetchType.LAZY)
    private Set<Shift> assignedShifts = new HashSet<>();

    @AssertTrue
    public boolean isExperiencedEnough() {
        return getYearsOfExperience() >= MIN_YEARS_OF_EXPERIENCE;
    }

}
