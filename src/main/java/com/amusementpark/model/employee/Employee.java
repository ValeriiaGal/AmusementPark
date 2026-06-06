package com.amusementpark.model.employee;

import com.amusementpark.model.Shift;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Min(0)
    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    @NotNull
    @Past
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Builder.Default
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Shift> shifts = new HashSet<>();
}
