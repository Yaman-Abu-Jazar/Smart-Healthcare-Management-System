package com.exalt.healthcare.domain.service.implementation;

import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.PatientRepository;
import com.exalt.healthcare.domain.service.implementations.PatientServiceImpl;
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
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient testPatient;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@email.com")
                .password("password123")
                .role(Role.PATIENT)
                .deleted(false)
                .build();

        // Create test patient
        testPatient = Patient.builder()
                .id(1L)
                .first_name("Alice")
                .last_name("Johnson")
                .address("123 Main Street, City")
                .user(testUser)
                .build();
    }

    @Test
    void savePatient_ShouldReturnSavedPatient() {
        // Given
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient savedPatient = patientService.savePatient(testPatient);

        // Then
        assertNotNull(savedPatient);
        assertEquals(testPatient.getId(), savedPatient.getId());
        assertEquals(testPatient.getFirst_name(), savedPatient.getFirst_name());
        assertEquals(testPatient.getLast_name(), savedPatient.getLast_name());
        assertEquals(testPatient.getAddress(), savedPatient.getAddress());

        verify(patientRepository, times(1)).save(testPatient);
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() {
        // Given
        Patient patient2 = Patient.builder()
                .id(2L)
                .first_name("Bob")
                .last_name("Smith")
                .address("456 Oak Avenue, City")
                .build();

        List<Patient> expectedPatients = Arrays.asList(testPatient, patient2);
        when(patientRepository.findAll()).thenReturn(expectedPatients);

        // When
        List<Patient> actualPatients = patientService.getAllPatients();

        // Then
        assertNotNull(actualPatients);
        assertEquals(2, actualPatients.size());
        assertTrue(actualPatients.contains(testPatient));
        assertTrue(actualPatients.contains(patient2));

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getAllPatients_WithEmptyDatabase_ShouldReturnEmptyList() {
        // Given
        when(patientRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Patient> actualPatients = patientService.getAllPatients();

        // Then
        assertNotNull(actualPatients);
        assertTrue(actualPatients.isEmpty());

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_WithValidId_ShouldReturnPatient() {
        // Given
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(testPatient));

        // When
        Patient foundPatient = patientService.getPatientById(patientId);

        // Then
        assertNotNull(foundPatient);
        assertEquals(testPatient.getId(), foundPatient.getId());
        assertEquals(testPatient.getFirst_name(), foundPatient.getFirst_name());
        assertEquals(testPatient.getLast_name(), foundPatient.getLast_name());

        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void getPatientById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 999L;
        when(patientRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        PatientNotFoundException exception = assertThrows(
                PatientNotFoundException.class,
                () -> patientService.getPatientById(invalidId)
        );

        assertEquals("Patient not found with id: " + invalidId, exception.getMessage());
        verify(patientRepository, times(1)).findById(invalidId);
    }

    @Test
    void updatePatient_WithValidId_ShouldReturnUpdatedPatient() {
        // Given
        Long patientId = 1L;
        Patient updatedDetails = Patient.builder()
                .first_name("Alice Updated")
                .last_name("Johnson Updated")
                .address("789 New Street, New City")
                .user(testUser)
                .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient updatedPatient = patientService.updatePatient(patientId, updatedDetails);

        // Then
        assertNotNull(updatedPatient);
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).save(testPatient);

        // Verify that the patient details were updated
        assertEquals(updatedDetails.getFirst_name(), testPatient.getFirst_name());
        assertEquals(updatedDetails.getLast_name(), testPatient.getLast_name());
        assertEquals(updatedDetails.getAddress(), testPatient.getAddress());
    }

    @Test
    void updatePatient_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 999L;
        Patient updatedDetails = Patient.builder()
                .first_name("Updated Name")
                .build();

        when(patientRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        PatientNotFoundException exception = assertThrows(
                PatientNotFoundException.class,
                () -> patientService.updatePatient(invalidId, updatedDetails)
        );

        assertEquals("Patient not found with id: " + invalidId, exception.getMessage());
        verify(patientRepository, times(1)).findById(invalidId);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deletePatient_WithValidId_ShouldDeletePatient() {
        // Given
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(testPatient));
        doNothing().when(patientRepository).deleteById(patientId);

        // When
        assertDoesNotThrow(() -> patientService.deletePatient(patientId));

        // Then
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).deleteById(patientId);
    }

    @Test
    void deletePatient_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 999L;
        when(patientRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        PatientNotFoundException exception = assertThrows(
                PatientNotFoundException.class,
                () -> patientService.deletePatient(invalidId)
        );

        assertEquals("Patient not found with id: " + invalidId, exception.getMessage());
        verify(patientRepository, times(1)).findById(invalidId);
        verify(patientRepository, never()).deleteById(anyLong());
    }

    @Test
    void savePatient_WithNullPatient_ShouldHandleGracefully() {
        // Given
        when(patientRepository.save(null)).thenThrow(new IllegalArgumentException("Patient cannot be null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.savePatient(null));
    }

    @Test
    void updatePatient_WithPartialUpdate_ShouldUpdateOnlyProvidedFields() {
        // Given
        Long patientId = 1L;
        Patient partialUpdate = Patient.builder()
                .address("New Address Only")
                .build();

        Patient existingPatient = Patient.builder()
                .id(1L)
                .first_name("Original First")
                .last_name("Original Last")
                .address("Original Address")
                .user(testUser)
                .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

        // When
        Patient updatedPatient = patientService.updatePatient(patientId, partialUpdate);

        // Then
        assertNotNull(updatedPatient);
        assertEquals("New Address Only", existingPatient.getAddress());
        // Other fields should remain unchanged if null in update
        verify(patientRepository, times(1)).save(existingPatient);
    }
}
