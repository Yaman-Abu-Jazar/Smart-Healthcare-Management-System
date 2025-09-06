package com.exalt.healthcare.domain.valueobject;

import lombok.Getter;

@Getter

public enum AppointmentStatus {
    SCHEDULED(0, "scheduled"),
    COMPLETED(1, "completed"),
    CANCELLED(2, "cancelled");

    private final int level;
    private final String description;

    AppointmentStatus(int level, String description){
        this.level = level;
        this.description = description;
    }
}
