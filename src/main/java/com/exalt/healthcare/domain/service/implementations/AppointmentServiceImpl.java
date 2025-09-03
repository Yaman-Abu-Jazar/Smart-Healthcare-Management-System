package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.repository.jpa.AppointmentRepository;
import com.exalt.healthcare.domain.service.interfaces.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
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
    public void DeleteAppointment(Long id) throws AppointmentNotFoundException {
        Appointment appointment = this.repository.findById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByData(LocalDate date) {
        return List.of();
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return List.of();
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return List.of();
    }
}
