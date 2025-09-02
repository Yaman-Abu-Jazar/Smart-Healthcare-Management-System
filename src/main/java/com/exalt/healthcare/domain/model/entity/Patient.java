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
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private long id;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ConstructorProperties({"first_name", "last_name", "address", "user"})
    public Patient(String first_name, String last_name, String address, User user){
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.user = user;
    }
}
