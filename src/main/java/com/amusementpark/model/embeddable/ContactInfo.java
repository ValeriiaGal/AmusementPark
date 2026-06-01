package com.amusementpark.model.embeddable;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ContactInfo {

    @NotBlank
    @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$")
    private String phone;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
}
