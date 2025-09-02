package com.exalt.healthcare.domain.model.entity;

import com.exalt.healthcare.enums.Role;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long user_id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @ConstructorProperties({"email", "password", "role"})
    public User(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
