package com.exalt.healthcare.common.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDto {
    private Long patientId;
    private List<String> medications;
    private List<String> notes;
    private LocalDateTime createdAt;
}
