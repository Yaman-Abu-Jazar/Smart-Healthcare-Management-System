package com.exalt.healthcare.domain.service.interfaces;


import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment addNewAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    void deleteAppointment(Long id);
    List<Appointment> getAppointmentsByDate(LocalDate date);
    List<Appointment> getAppointmentsByDoctor(Doctor doctor);
    List<Appointment> getAppointmentsByPatient(Patient patient);
    Appointment updateAppointment(Long id, Appointment appointment);
    Appointment completeAppointment(Long appointmentId, Long doctorId);
}
