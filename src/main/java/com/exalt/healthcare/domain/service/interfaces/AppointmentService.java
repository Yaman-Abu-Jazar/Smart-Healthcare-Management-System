package com.exalt.healthcare.domain.service.interfaces;


import com.exalt.healthcare.common.payload.AppointmentDto;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment addNewAppointment(AppointmentDto appointment);
    List<Appointment> getAllAppointments();
    void deleteAppointment(Long id);
    List<Appointment> getAppointmentsByDate(LocalDate date);
    List<Appointment> getAppointmentsByDoctorId(Long doctorId);
    List<Appointment> getAppointmentsByPatientId(Long patientId);
    Appointment updateAppointment(Long id, Appointment appointment);
    Appointment completeAppointment(Long appointmentId) ;
    Appointment bookAppointment(Long appointmentId);
    Appointment cancelAppointment(Long appointmentId);
    Appointment cancelAppointmentByDoctor(Long appointmentId);
    List<Appointment> getPatientAppointments();
}
