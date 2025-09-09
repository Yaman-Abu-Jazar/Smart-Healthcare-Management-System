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
    List<Appointment> findAppointmentsByDateAndDoctor_Id(LocalDate date, Long doctorId);
    List<Appointment> findAppointmentsByDoctor_Id(Long doctorId);
    List<Appointment> findAppointmentsByPatient_Id(Long patientId);
}
