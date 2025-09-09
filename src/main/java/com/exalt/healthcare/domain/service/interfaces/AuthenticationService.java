package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.common.payload.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(UserDto request);
    AuthenticationResponse registerDoctor(DoctorDto request);
    AuthenticationResponse registerPatient(PatientDto request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    AuthenticationResponse updateDoctor(Long id, DoctorDto dto);
    AuthenticationResponse updatePatient(Long id, PatientDto dto);
    AuthenticationResponse updateUser(Long id, UserDto dto);
}
