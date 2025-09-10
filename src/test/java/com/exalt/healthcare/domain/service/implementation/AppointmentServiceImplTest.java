package com.exalt.healthcare.domain.service.implementation;

import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.common.exception.UserNotFoundException;
import com.exalt.healthcare.common.payload.AppointmentDto;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.AppointmentRepository;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.repository.jpa.PatientRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.service.implementations.AppointmentServiceImpl;
import com.exalt.healthcare.domain.valueobject.AppointmentStatus;
import com.exalt.healthcare.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Doctor testDoctor;
    private Patient testPatient;
    private User doctorUser;
    private User patientUser;
    private Appointment testAppointment;
    private AppointmentDto testAppointmentDto;

    @BeforeEach
    void setUp() {
        // Create test users
        doctorUser = User.builder()
                .id(1L)
                .firstName("Dr. John")
                .lastName("Smith")
                .email("dr.smith@hospital.com")
                .role(Role.DOCTOR)
                .deleted(false)
                .build();

        patientUser = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@email.com")
                .role(Role.PATIENT)
                .deleted(false)
                .build();

        // Create test doctor
        testDoctor = Doctor.builder()
                .id(1L)
                .first_name("John")
                .last_name("Smith")
                .specialty("Cardiology")
                .user(doctorUser)
                .phone("1234567890")
                .build();

        // Create test patient
        testPatient = Patient.builder()
                .id(1L)
                .first_name("Jane")
                .last_name("Doe")
                .address("123 Main St")
                .user(patientUser)
                .build();

        // Create test appointment
        testAppointment = Appointment.builder()
                .id(1L)
                .date(LocalDate.now().plusDays(1))
                .startTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0))
                .status(AppointmentStatus.SCHEDULED)
                .doctor(testDoctor)
                .patient(null)
                .notes("Regular checkup")
                .build();

        // Create test appointment DTO
        testAppointmentDto = new AppointmentDto();
        testAppointmentDto.setDoctorId(1L);
        testAppointmentDto.setDate(LocalDate.now().plusDays(1));
        testAppointmentDto.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        testAppointmentDto.setEndTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
        testAppointmentDto.setNotes("Regular checkup");
    }

    @Test
    void addNewAppointment_WithValidData_ShouldReturnSavedAppointment() {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        Appointment result = appointmentService.addNewAppointment(testAppointmentDto);

        // Then
        assertNotNull(result);
        assertEquals(testAppointment.getId(), result.getId());
        assertEquals(testDoctor, result.getDoctor());
        assertEquals(AppointmentStatus.SCHEDULED, result.getStatus());
        assertEquals(testAppointmentDto.getDate(), result.getDate());
        assertEquals(testAppointmentDto.getStartTime(), result.getStartTime());
        assertEquals(testAppointmentDto.getEndTime(), result.getEndTime());

        verify(doctorRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void addNewAppointment_WithInvalidDoctorId_ShouldThrowException() {
        // Given
        Long invalidDoctorId = 999L;
        testAppointmentDto.setDoctorId(invalidDoctorId);
        when(doctorRepository.findById(invalidDoctorId)).thenReturn(Optional.empty());

        // When & Then
        DoctorNotFoundException exception = assertThrows(
                DoctorNotFoundException.class,
                () -> appointmentService.addNewAppointment(testAppointmentDto)
        );

        assertEquals("Doctor not found with id : " + invalidDoctorId, exception.getMessage());
        verify(doctorRepository, times(1)).findById(invalidDoctorId);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void getAllAppointments_ShouldReturnDoctorAppointments() {
        // Given
        String doctorEmail = "dr.smith@hospital.com";
        List<Appointment> expectedAppointments = Arrays.asList(testAppointment);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(doctorEmail);
            when(userRepository.findByEmail(doctorEmail)).thenReturn(Optional.of(doctorUser));
            when(doctorRepository.findByUser(doctorUser)).thenReturn(Optional.of(testDoctor));
            when(appointmentRepository.findAppointmentsByDoctor_Id(1L)).thenReturn(expectedAppointments);

            // When
            List<Appointment> result = appointmentService.getAllAppointments();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(testAppointment, result.get(0));

            verify(userRepository, times(1)).findByEmail(doctorEmail);
            verify(doctorRepository, times(1)).findByUser(doctorUser);
            verify(appointmentRepository, times(1)).findAppointmentsByDoctor_Id(1L);
        }
    }

    @Test
    void bookAppointment_WithValidId_ShouldBookSuccessfully() {
        // Given
        Long appointmentId = 1L;
        String patientEmail = "jane.doe@email.com";

        testAppointment.setStatus(AppointmentStatus.SCHEDULED);
        testAppointment.setPatient(null);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(patientEmail);
            when(userRepository.findByEmail(patientEmail)).thenReturn(Optional.of(patientUser));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
            when(patientRepository.findByUser(patientUser)).thenReturn(Optional.of(testPatient));
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

            // When
            Appointment result = appointmentService.bookAppointment(appointmentId);

            // Then
            assertNotNull(result);
            assertEquals(AppointmentStatus.BUSY, testAppointment.getStatus());
            assertEquals(testPatient, testAppointment.getPatient());

            verify(appointmentRepository, times(1)).save(testAppointment);
        }
    }

    @Test
    void bookAppointment_WithNonScheduledAppointment_ShouldThrowException() {
        // Given
        Long appointmentId = 1L;
        String patientEmail = "jahn.doe@email.com";

        testAppointment.setStatus(AppointmentStatus.BUSY); // Already booked

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(patientEmail);
            when(userRepository.findByEmail(patientEmail)).thenReturn(Optional.of(patientUser));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
            when(patientRepository.findByUser(patientUser)).thenReturn(Optional.of(testPatient));

            // When & Then
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> appointmentService.bookAppointment(appointmentId)
            );

            assertTrue(exception.getMessage().contains("is not available"));
            verify(appointmentRepository, never()).save(any(Appointment.class));
        }
    }

    @Test
    void cancelAppointment_WithValidId_ShouldCancelSuccessfully() {
        // Given
        Long appointmentId = 1L;
        String patientEmail = "jane.doe@email.com";

        testAppointment.setStatus(AppointmentStatus.BUSY);
        testAppointment.setPatient(testPatient);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(patientEmail);
            when(userRepository.findByEmail(patientEmail)).thenReturn(Optional.of(patientUser));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
            when(patientRepository.findByUser(patientUser)).thenReturn(Optional.of(testPatient));
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

            // When
            Appointment result = appointmentService.cancelAppointment(appointmentId);

            // Then
            assertNotNull(result);
            assertEquals(AppointmentStatus.SCHEDULED, testAppointment.getStatus());
            assertNull(testAppointment.getPatient());

            verify(appointmentRepository, times(1)).save(testAppointment);
        }
    }

    @Test
    void completeAppointment_WithValidDoctor_ShouldCompleteSuccessfully() {
        // Given
        Long appointmentId = 1L;
        String doctorEmail = "dr.smith@hospital.com";

        testAppointment.setStatus(AppointmentStatus.BUSY);
        testAppointment.setPatient(testPatient);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(doctorEmail);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
            when(userRepository.findByEmail(doctorEmail)).thenReturn(Optional.of(doctorUser));
            when(doctorRepository.findByUser(doctorUser)).thenReturn(Optional.of(testDoctor));
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

            // When
            Appointment result = appointmentService.completeAppointment(appointmentId);

            // Then
            assertNotNull(result);
            assertEquals(AppointmentStatus.COMPLETED, testAppointment.getStatus());

            verify(appointmentRepository, times(1)).save(testAppointment);
        }
    }

    @Test
    void completeAppointment_WithWrongDoctor_ShouldThrowException() {
        // Given
        Long appointmentId = 1L;
        String wrongDoctorEmail = "wrong.doctor@hospital.com";

        User wrongDoctorUser = User.builder()
                .id(3L)
                .email(wrongDoctorEmail)
                .role(Role.DOCTOR)
                .build();

        Doctor wrongDoctor = Doctor.builder()
                .id(2L)
                .user(wrongDoctorUser)
                .build();

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(wrongDoctorEmail);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
            when(userRepository.findByEmail(wrongDoctorEmail)).thenReturn(Optional.of(wrongDoctorUser));
            when(doctorRepository.findByUser(wrongDoctorUser)).thenReturn(Optional.of(wrongDoctor));

            // When & Then
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> appointmentService.completeAppointment(appointmentId)
            );

            assertEquals("You can only complete your own appointments", exception.getMessage());
            verify(appointmentRepository, never()).save(any(Appointment.class));
        }
    }

    @Test
    void deleteAppointment_WithValidId_ShouldDeleteSuccessfully() {
        // Given
        Long appointmentId = 1L;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
        doNothing().when(appointmentRepository).delete(testAppointment);

        // When
        assertDoesNotThrow(() -> appointmentService.deleteAppointment(appointmentId));

        // Then
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).delete(testAppointment);
    }

    @Test
    void deleteAppointment_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 999L;
        when(appointmentRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        AppointmentNotFoundException exception = assertThrows(
                AppointmentNotFoundException.class,
                () -> appointmentService.deleteAppointment(invalidId)
        );

        assertEquals("No Appointment with this id : " + invalidId, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(invalidId);
        verify(appointmentRepository, never()).delete(any(Appointment.class));
    }

    @Test
    void getAppointmentsByDate_ShouldReturnAppointmentsForDate() {
        // Given
        LocalDate testDate = LocalDate.now();
        List<Appointment> expectedAppointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findAppointmentsByDate(testDate)).thenReturn(expectedAppointments);

        // When
        List<Appointment> result = appointmentService.getAppointmentsByDate(testDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAppointment, result.get(0));

        verify(appointmentRepository, times(1)).findAppointmentsByDate(testDate);
    }

    @Test
    void getAppointmentsByDoctorId_ShouldReturnDoctorAppointments() {
        // Given
        Long doctorId = 1L;
        List<Appointment> expectedAppointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findAppointmentsByDoctor_Id(doctorId)).thenReturn(expectedAppointments);

        // When
        List<Appointment> result = appointmentService.getAppointmentsByDoctorId(doctorId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAppointment, result.get(0));

        verify(appointmentRepository, times(1)).findAppointmentsByDoctor_Id(doctorId);
    }

    @Test
    void getAppointmentsByPatientId_ShouldReturnPatientAppointments() {
        // Given
        Long patientId = 1L;
        List<Appointment> expectedAppointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findAppointmentsByPatient_Id(patientId)).thenReturn(expectedAppointments);

        // When
        List<Appointment> result = appointmentService.getAppointmentsByPatientId(patientId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAppointment, result.get(0));

        verify(appointmentRepository, times(1)).findAppointmentsByPatient_Id(patientId);
    }

    @Test
    void updateAppointment_WithValidId_ShouldUpdateSuccessfully() {
        // Given
        Long appointmentId = 1L;
        LocalDate newDate = LocalDate.now().plusDays(2);

        Appointment updatedData = Appointment.builder()
                .date(newDate)
                .startTime(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(2).withHour(15).withMinute(0))
                .status(AppointmentStatus.SCHEDULED)
                .doctor(testDoctor)
                .notes("Updated notes")
                .build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        Appointment result = appointmentService.updateAppointment(appointmentId, updatedData);

        // Then
        assertNotNull(result);
        assertEquals(newDate, testAppointment.getDate());
        assertEquals(updatedData.getStartTime(), testAppointment.getStartTime());
        assertEquals(updatedData.getEndTime(), testAppointment.getEndTime());
        assertEquals(updatedData.getNotes(), testAppointment.getNotes());

        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(testAppointment);
    }

    @Test
    void updateAppointment_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidId = 999L;
        when(appointmentRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        AppointmentNotFoundException exception = assertThrows(
                AppointmentNotFoundException.class,
                () -> appointmentService.updateAppointment(invalidId, testAppointment)
        );

        assertEquals("No Appointment with this id : " + invalidId, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(invalidId);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_WithNoConflicts_ShouldBookSuccessfully() {
        // Given
        Long appointmentId = 1L;
        String patientEmail = "jane.doe@email.com";

        testAppointment.setStatus(AppointmentStatus.SCHEDULED);
        testAppointment.setPatient(null);

        List<Appointment> myAppointments = Arrays.asList(
                Appointment.builder()
                        .id(2L)
                        .date(testAppointment.getDate())
                        .startTime(testAppointment.getStartTime().minusHours(2))
                        .endTime(testAppointment.getStartTime().minusHours(1)) // ends before new one
                        .status(AppointmentStatus.BUSY)
                        .patient(testPatient)
                        .build()
        );

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(patientEmail);
            when(userRepository.findByEmail(patientEmail)).thenReturn(Optional.of(patientUser));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
            when(patientRepository.findByUser(patientUser)).thenReturn(Optional.of(testPatient));
            when(appointmentRepository.findAppointmentsByPatient_Id(testPatient.getId())).thenReturn(myAppointments);
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

            // When
            Appointment result = appointmentService.bookAppointment(appointmentId);

            // Then
            assertNotNull(result);
            assertEquals(AppointmentStatus.BUSY, result.getStatus());
            assertEquals(testPatient, result.getPatient());
            verify(appointmentRepository, times(1)).save(testAppointment);
        }
    }

    @Test
    void bookAppointment_WithConflictingAppointment_ShouldThrowException() {
        // Given
        Long appointmentId = 1L;
        String patientEmail = "jane.doe@email.com";

        testAppointment.setStatus(AppointmentStatus.SCHEDULED);
        testAppointment.setPatient(null);

        // conflicting appointment at the same time
        Appointment conflicting = Appointment.builder()
                .id(2L)
                .date(testAppointment.getDate())
                .startTime(testAppointment.getStartTime()) // same start time
                .endTime(testAppointment.getEndTime())
                .status(AppointmentStatus.BUSY)
                .patient(testPatient)
                .build();

        List<Appointment> myAppointments = Collections.singletonList(conflicting);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(patientEmail);
            when(userRepository.findByEmail(patientEmail)).thenReturn(Optional.of(patientUser));
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(testAppointment));
            when(patientRepository.findByUser(patientUser)).thenReturn(Optional.of(testPatient));
            when(appointmentRepository.findAppointmentsByPatient_Id(testPatient.getId())).thenReturn(myAppointments);

            // When & Then
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> appointmentService.bookAppointment(appointmentId)
            );

            assertTrue(exception.getMessage().contains("contradiction"));
            verify(appointmentRepository, never()).save(any(Appointment.class));
        }
    }

}