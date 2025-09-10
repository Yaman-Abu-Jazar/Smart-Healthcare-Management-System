package com.exalt.healthcare.domain.service.implementation;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.service.implementations.DoctorServiceImpl;
import com.exalt.healthcare.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor testDoctor;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@hospital.com")
                .password("password123")
                .role(Role.DOCTOR)
                .deleted(false)
                .build();

        // Create test doctor
        testDoctor = Doctor.builder()
                .id(1L)
                .first_name("John")
                .last_name("Doe")
                .specialty("Cardiology")
                .user(testUser)
                .phone("1234567890")
                .build();
    }

    @Test
    void addingNewDoctor_ShouldReturnSavedDoctor() {
        // Given
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        // When
        Doctor savedDoctor = doctorService.addingNewDoctor(testDoctor);

        // Then
        assertNotNull(savedDoctor);
        assertEquals(testDoctor.getId(), savedDoctor.getId());
        assertEquals(testDoctor.getFirst_name(), savedDoctor.getFirst_name());
        assertEquals(testDoctor.getLast_name(), savedDoctor.getLast_name());
        assertEquals(testDoctor.getSpecialty(), savedDoctor.getSpecialty());

        verify(doctorRepository, times(1)).save(testDoctor);
    }

    @Test
    void findDoctorsBySpecialty_ShouldReturnDoctorsWithSpecialty() {
        // Given
        String specialty = "Cardiology";
        List<Doctor> expectedDoctors = Arrays.asList(testDoctor);
        when(doctorRepository.findDoctorBySpecialty(specialty)).thenReturn(expectedDoctors);

        // When
        List<Doctor> actualDoctors = doctorService.findDoctorsBySpecialty(specialty);

        // Then
        assertNotNull(actualDoctors);
        assertEquals(1, actualDoctors.size());
        assertEquals(testDoctor.getSpecialty(), actualDoctors.get(0).getSpecialty());

        verify(doctorRepository, times(1)).findDoctorBySpecialty(specialty);
    }

    @Test
    void findDoctorsBySpecialty_WithEmptyResult_ShouldReturnEmptyList() {
        // Given
        String specialty = "Neurology";
        when(doctorRepository.findDoctorBySpecialty(specialty)).thenReturn(Arrays.asList());

        // When
        List<Doctor> actualDoctors = doctorService.findDoctorsBySpecialty(specialty);

        // Then
        assertNotNull(actualDoctors);
        assertTrue(actualDoctors.isEmpty());

        verify(doctorRepository, times(1)).findDoctorBySpecialty(specialty);
    }

    @Test
    void findDoctorById_WithValidId_ShouldReturnDoctor() {
        // Given
        Long doctorId = 1L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(testDoctor));

        // When
        Doctor foundDoctor = doctorService.findDoctorById(doctorId);

        // Then
        assertNotNull(foundDoctor);
        assertEquals(testDoctor.getId(), foundDoctor.getId());
        assertEquals(testDoctor.getFirst_name(), foundDoctor.getFirst_name());

        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void findDoctorById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 999L;
        when(doctorRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        DoctorNotFoundException exception = assertThrows(
                DoctorNotFoundException.class,
                () -> doctorService.findDoctorById(invalidId)
        );

        assertEquals("Doctor not found with id: " + invalidId, exception.getMessage());
        verify(doctorRepository, times(1)).findById(invalidId);
    }

    @Test
    void findAllDoctors_ShouldReturnAllDoctors() {
        // Given
        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .first_name("Jane")
                .last_name("Smith")
                .specialty("Pediatrics")
                .build();

        List<Doctor> expectedDoctors = Arrays.asList(testDoctor, doctor2);
        when(doctorRepository.findAll()).thenReturn(expectedDoctors);

        // When
        List<Doctor> actualDoctors = doctorService.findAllDoctors();

        // Then
        assertNotNull(actualDoctors);
        assertEquals(2, actualDoctors.size());
        assertTrue(actualDoctors.contains(testDoctor));
        assertTrue(actualDoctors.contains(doctor2));

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void findAllDoctors_WithEmptyDatabase_ShouldReturnEmptyList() {
        // Given
        when(doctorRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Doctor> actualDoctors = doctorService.findAllDoctors();

        // Then
        assertNotNull(actualDoctors);
        assertTrue(actualDoctors.isEmpty());

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void deleteDoctorById_WithValidId_ShouldDeleteDoctor() {
        // Given
        Long doctorId = 1L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(testDoctor));
        doNothing().when(doctorRepository).deleteById(doctorId);

        // When
        assertDoesNotThrow(() -> doctorService.deleteDoctorById(doctorId));

        // Then
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(doctorRepository, times(1)).deleteById(doctorId);
    }

    @Test
    void deleteDoctorById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 999L;
        when(doctorRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        DoctorNotFoundException exception = assertThrows(
                DoctorNotFoundException.class,
                () -> doctorService.deleteDoctorById(invalidId)
        );

        assertEquals("Doctor not found with id: " + invalidId, exception.getMessage());
        verify(doctorRepository, times(1)).findById(invalidId);
        verify(doctorRepository, never()).deleteById(anyLong());
    }

    @Test
    void addingNewDoctor_WithNullDoctor_ShouldHandleGracefully() {
        // Given
        when(doctorRepository.save(null)).thenThrow(new IllegalArgumentException("Doctor cannot be null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> doctorService.addingNewDoctor(null));
    }
}
