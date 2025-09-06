package com.exalt.healthcare.domain.repository.jpa;

import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends EntityRepository<Appointment, Long> {
    List<Appointment> findAppointmentsByDate(LocalDate date);
    List<Appointment> findAppointmentsByDoctor(Doctor doctor);
    List<Appointment> findAppointmentsByPatient(Patient patient);
}
