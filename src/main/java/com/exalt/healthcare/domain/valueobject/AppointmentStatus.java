package com.exalt.healthcare.domain.valueobject;

import lombok.Getter;

@Getter

public enum AppointmentStatus {
    SCHEDULED(0, "scheduled"),
    BUSY(1, "busy"),
    COMPLETED(2, "completed"),
    CANCELLED(3, "cancelled");

    private final int level;
    private final String description;

    AppointmentStatus(int level, String description){
        this.level = level;
        this.description = description;
    }
}
