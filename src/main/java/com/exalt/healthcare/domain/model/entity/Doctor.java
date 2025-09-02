package com.exalt.healthcare.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private long id;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ConstructorProperties({"first_name", "last_name", "specialty", "user"})
    public Doctor(String first_name, String last_name, String specialty, User user){
        this.first_name = first_name;
        this.last_name = last_name;
        this.specialty = specialty;
        this.user = user;
    }
}
