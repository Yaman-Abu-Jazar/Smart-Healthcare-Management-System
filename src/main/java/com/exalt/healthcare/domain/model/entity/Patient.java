package com.exalt.healthcare.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.beans.ConstructorProperties;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private long id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String first_name;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String last_name;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ConstructorProperties({"first_name", "last_name", "address", "user"})
    public Patient(String first_name, String last_name, String address, User user){
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.user = user;
    }
}
