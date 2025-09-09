package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.repository.jpa.AppointmentRepository;
import com.exalt.healthcare.domain.service.interfaces.AppointmentService;
import com.exalt.healthcare.domain.valueobject.AppointmentStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository repository){
        this.repository = repository;
    }

    @Override
    public Appointment addNewAppointment(Appointment appointment) {
        return this.repository.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return this.repository.findAll();
    }

    @Override
    public void deleteAppointment(Long id) throws AppointmentNotFoundException {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("No Appointment with this id : " + id));
        this.repository.delete(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return this.repository.findAppointmentsByDate(date);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return this.repository.findAppointmentsByDoctor(doctor);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return this.repository.findAppointmentsByPatient(patient);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) throws AppointmentNotFoundException {
        Appointment updatedAppointment = this.repository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("No Appointment with this id : " + id));
        updatedAppointment.setDate(appointment.getDate());
        updatedAppointment.setDoctor(appointment.getDoctor());
        updatedAppointment.setStatus(appointment.getStatus());
        updatedAppointment.setNotes(appointment.getNotes());
        updatedAppointment.setPatient(appointment.getPatient());
        return this.repository.save(updatedAppointment);
    }

    @Override
    public Appointment completeAppointment(Long appointmentId, Long doctorId) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + appointmentId));

        // Verify the doctor owns this appointment
        if (appointment.getDoctor().getId() != doctorId) {
            throw new RuntimeException("You can only complete your own appointments");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setCompletedAt(LocalDateTime.now());

        return repository.save(appointment);
    }

    @Override
    public Appointment bookAppointment(Long appointmentId, Long doctorId) {
        return null;
    }
}
