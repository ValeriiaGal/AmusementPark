package com.amusementpark.model.employee;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ride_operator")
public class RideOperator extends Employee{

    @NotBlank
    @Column(name = "operator_licence_id", unique = true)
    private String operatorLicenceId;

    @Column(name = "first_aid_certified")
    private boolean firstAidCertified;
}
