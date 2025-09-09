package com.exalt.healthcare.common.payload;

import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.valueobject.Role;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDto {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private String specialty;
    private String phone;
}
