package com.exalt.healthcare.domain.service.implementation;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.common.exception.UserNotFoundException;
import com.exalt.healthcare.common.payload.AuthenticationResponse;
import com.exalt.healthcare.common.payload.DoctorDto;
import com.exalt.healthcare.common.payload.UserDto;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
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
public class AuthenticationServiceImplDoctorTest {

    @Mock private UserRepository userRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private DoctorDto doctorDto;
    private User user;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctorDto = DoctorDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@hospital.com")
                .password("rawPassword")
                .role(Role.DOCTOR)
                .specialty("Cardiology")
                .phone("1234567890")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@hospital.com")
                .password("encodedPassword")
                .role(Role.DOCTOR)
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .first_name("John")
                .last_name("Doe")
                .specialty("Cardiology")
                .phone("1234567890")
                .user(user)
                .build();
    }

    @Test
    void registerDoctor_ShouldCreateDoctorAndReturnTokens() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh-token");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // when
        AuthenticationResponse response = authenticationService.registerDoctor(doctorDto);

        // then
        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(userRepository, times(1)).save(any(User.class));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
        verify(tokenRepository, times(1)).save(any()); // token persisted
    }

    @Test
    void registerDoctor_WithInvalidRole_ShouldThrowException() {
        // given invalid role
        doctorDto.setRole(Role.PATIENT);

        // when & then
        var ex = assertThrows(Exception.class, () -> authenticationService.registerDoctor(doctorDto));
        assertTrue(ex.getMessage().contains("Only DOCTOR role can register here"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateDoctor_ShouldUpdateAndReturnTokens() {
        // given
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("new-jwt");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("new-refresh");

        // when
        AuthenticationResponse response = authenticationService.updateDoctor(1L, doctorDto);

        // then
        assertNotNull(response);
        assertEquals("new-jwt", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());

        verify(doctorRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmail(doctorDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_WithInvalidId_ShouldThrowException() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,
                () -> authenticationService.updateDoctor(99L, doctorDto));
    }

    @Test
    void updateDoctor_WithInvalidEmail_ShouldThrowException() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authenticationService.updateDoctor(1L, doctorDto));
    }
}

