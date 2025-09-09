package com.exalt.healthcare.common.payload;

import com.exalt.healthcare.domain.valueobject.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long doctorId;
    private String notes;
}
