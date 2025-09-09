package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.common.payload.*;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.common.exception.UserNotFoundException;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.repository.jpa.PatientRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.service.interfaces.AuthenticationService;
import com.exalt.healthcare.domain.valueobject.Role;
import com.exalt.healthcare.infrastructure.security.JwtService;
import com.exalt.healthcare.infrastructure.token.Token;
import com.exalt.healthcare.infrastructure.token.TokenRepository;
import com.exalt.healthcare.infrastructure.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(UserDto request) {
        if(!(request.getRole() == Role.ADMIN)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only ADMIN role can register here");
        }
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse registerDoctor(DoctorDto request) {
        if(!(request.getRole() == Role.DOCTOR)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only DOCTOR role can register here");
        }
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        var doctor = Doctor.builder()
                .first_name(request.getFirstname())
                .last_name(request.getLastname())
                .specialty(request.getSpecialty())
                .user(user)
                .phone(request.getPhone())
                .build();
        doctorRepository.save(doctor);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse registerPatient(PatientDto request) {
        if(!(request.getRole() == Role.PATIENT)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PATIENT role can register here");
        }
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        var patient = Patient.builder()
                .first_name(request.getFirstname())
                .last_name(request.getLastname())
                .address(request.getAddress())
                .user(user)
                .build();
        patientRepository.save(patient);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + request.getEmail()));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUserEmail(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email : " + userEmail));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public AuthenticationResponse updateDoctor(Long id, DoctorDto dto) {
        Doctor updatedDoctor = this.doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));

        updatedDoctor.setFirst_name(dto.getFirstname());
        updatedDoctor.setLast_name(dto.getLastname());
        updatedDoctor.setSpecialty(dto.getSpecialty());
        updatedDoctor.setPhone(dto.getPhone());

        User updatedUser = this.userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + dto.getEmail()));

        updatedUser.setFirstName(dto.getFirstname());
        updatedUser.setLastName(dto.getLastname());
        updatedUser.setEmail(dto.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        var savedUser = userRepository.save(updatedUser);
        var jwtToken = jwtService.generateToken(updatedUser);
        var refreshToken = jwtService.generateRefreshToken(updatedUser);
        saveUserToken(savedUser, jwtToken);

        doctorRepository.save(updatedDoctor);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse updatePatient(Long id, PatientDto dto) {
        Patient updatedPatient = this.patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

        updatedPatient.setFirst_name(dto.getFirstname());
        updatedPatient.setLast_name(dto.getLastname());
        updatedPatient.setAddress(dto.getAddress());

        User updatedUser = this.userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + dto.getEmail()));

        updatedUser.setFirstName(dto.getFirstname());
        updatedUser.setLastName(dto.getLastname());
        updatedUser.setEmail(dto.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        var savedUser = userRepository.save(updatedUser);
        var jwtToken = jwtService.generateToken(updatedUser);
        var refreshToken = jwtService.generateRefreshToken(updatedUser);
        saveUserToken(savedUser, jwtToken);

        patientRepository.save(updatedPatient);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse updateUser(Long id, UserDto dto){
        User updatedUser = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + id));

        updatedUser.setEmail(dto.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        updatedUser.setRole(dto.getRole());
        updatedUser.setFirstName(dto.getFirstname());
        updatedUser.setLastName(dto.getLastname());

        var savedUser = userRepository.save(updatedUser);
        var jwtToken = jwtService.generateToken(updatedUser);
        var refreshToken = jwtService.generateRefreshToken(updatedUser);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
