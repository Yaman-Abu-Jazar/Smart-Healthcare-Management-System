package com.exalt.healthcare.domain.model.entity;

import com.exalt.healthcare.domain.valueobject.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long user_id;

    @NotBlank
    @Column(name = "username", nullable = false)
    private String username;

    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    private boolean deleted;

    @ConstructorProperties({"username", "email", "password", "role", "deleted"})
    public User(String username, String email, String password, Role role, boolean deleted){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.deleted = deleted;
    }

}
