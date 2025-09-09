package com.exalt.healthcare.domain.valueobject;

import lombok.Getter;

@Getter

public enum Role {
    ADMIN(0, "admin_role"),
    DOCTOR(1, "doctor_role"),
    PATIENT(2, "patient_role");

    private final int level;
    private final String description;

    Role(int level, String description){
        this.level = level;
        this.description = description;
    }
}
