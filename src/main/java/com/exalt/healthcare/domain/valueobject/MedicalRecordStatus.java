package com.exalt.healthcare.domain.valueobject;

public enum MedicalRecordStatus {
    ACTIVE(0, "active"),
    ARCHIVED(1, "archived");

    private final int level;
    private final String description;

    MedicalRecordStatus(int level, String description){
        this.level = level;
        this.description = description;
    }
}
