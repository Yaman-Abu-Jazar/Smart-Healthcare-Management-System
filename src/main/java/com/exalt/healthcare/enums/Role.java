package com.exalt.healthcare.enums;

import lombok.Getter;

@Getter

public enum Role {
    ADMIN(0, "admin_role"),
    DOCTOR(1, "doctor"),
    PATIENT(2, "patient");

    private final int level;
    private final String description;

    Role(int level, String description){
        this.level = level;
        this.description = description;
    }
}
