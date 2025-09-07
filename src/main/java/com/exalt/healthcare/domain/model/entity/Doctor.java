package com.exalt.healthcare.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private long id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String first_name;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String last_name;

    @NotBlank
    @Column(name = "specialty", nullable = false)
    private String specialty;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "doctor")
    @ToString.Exclude
    private List<Appointment> appointmentList;

    @Size(max = 10)
    private String phone;

    @ConstructorProperties({"first_name", "last_name", "specialty", "user", "appointmentList", "phone"})
    public Doctor(String first_name, String last_name, String specialty, User user, List<Appointment> appointmentList, String phone){
        this.first_name = first_name;
        this.last_name = last_name;
        this.specialty = specialty;
        this.user = user;
        this.appointmentList = appointmentList;
        this.phone = phone;
    }
}
