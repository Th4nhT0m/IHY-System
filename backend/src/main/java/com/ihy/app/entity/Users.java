package com.ihy.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    @Column(nullable = false)
    String id;

    @Column
    @NotBlank
    String name;

    @Column
    @NotBlank // Forbidden: null, "", " "
    @Length(min = 8,message = "INVALID_PASSWORD")
    String password;

    @Column
    @NotBlank
    @Email
    String email;

    @Column
    String phone;

    @Column
    LocalDate birthday;

    @Column
    int isActive = 1; // Users can not be deleted but can only be disabled from the system via the isActive property

    @ManyToMany
    Set<Role> roles;

}
