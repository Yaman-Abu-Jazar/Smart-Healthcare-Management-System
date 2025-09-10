package com.exalt.healthcare.domain.service.implementation;

import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.common.exception.UserNotFoundException;
import com.exalt.healthcare.common.payload.AuthenticationResponse;
import com.exalt.healthcare.common.payload.PatientDto;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.PatientRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.service.implementations.AuthenticationServiceImpl;
import com.exalt.healthcare.domain.valueobject.Role;
import com.exalt.healthcare.infrastructure.security.JwtService;
import com.exalt.healthcare.infrastructure.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplPatientTest {

    @Mock private UserRepository userRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private PatientDto patientDto;
    private User user;
    private Patient patient;

    @BeforeEach
    void setUp() {
        patientDto = PatientDto.builder()
                .firstname("Alice")
                .lastname("Smith")
                .email("alice.smith@hospital.com")
                .password("rawPass")
                .role(Role.PATIENT)
                .address("123 Main St")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@hospital.com")
                .password("encodedPass")
                .role(Role.PATIENT)
                .build();

        patient = Patient.builder()
                .id(1L)
                .first_name("Alice")
                .last_name("Smith")
                .address("123 Main St")
                .user(user)
                .build();
    }

    @Test
    void registerPatient_ShouldCreatePatientAndReturnTokens() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-patient");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh-patient");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // when
        AuthenticationResponse response = authenticationService.registerPatient(patientDto);

        // then
        assertNotNull(response);
        assertEquals("jwt-patient", response.getAccessToken());
        assertEquals("refresh-patient", response.getRefreshToken());

        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void registerPatient_WithInvalidRole_ShouldThrowException() {
        patientDto.setRole(Role.DOCTOR); // invalid role

        var ex = assertThrows(Exception.class, () -> authenticationService.registerPatient(patientDto));
        assertTrue(ex.getMessage().contains("Only PATIENT role can register here"));

        verify(userRepository, never()).save(any());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void updatePatient_ShouldUpdateAndReturnTokens() {
        // given
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("new-jwt");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("new-refresh");

        // when
        AuthenticationResponse response = authenticationService.updatePatient(1L, patientDto);

        // then
        assertNotNull(response);
        assertEquals("new-jwt", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());

        verify(patientRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmail(patientDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WithInvalidId_ShouldThrowException() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> authenticationService.updatePatient(999L, patientDto));
    }

    @Test
    void updatePatient_WithInvalidEmail_ShouldThrowException() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authenticationService.updatePatient(1L, patientDto));
    }
}

