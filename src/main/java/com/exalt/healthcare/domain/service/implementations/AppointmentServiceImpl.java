package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.common.payload.AppointmentDto;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.repository.jpa.AppointmentRepository;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
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

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository){
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Appointment addNewAppointment(AppointmentDto appointment) {
        Doctor doctor = this.doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id : " + appointment.getDoctorId()));
        Appointment newAppointment = Appointment.builder()
                .doctor(doctor)
                .endTime(appointment.getEndTime())
                .startTime(appointment.getStartTime())
                .date(appointment.getDate())
                .notes(appointment.getNotes())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return this.appointmentRepository.save(newAppointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return this.appointmentRepository.findAll();
    }

    @Override
    public void deleteAppointment(Long id) throws AppointmentNotFoundException {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("No Appointment with this id : " + id));
        this.appointmentRepository.delete(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return this.appointmentRepository.findAppointmentsByDate(date);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return this.appointmentRepository.findAppointmentsByDoctorId(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return this.appointmentRepository.findAppointmentsByPatientId(patientId);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) throws AppointmentNotFoundException {
        Appointment updatedAppointment = this.appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("No Appointment with this id : " + id));
        updatedAppointment.setDate(appointment.getDate());
        updatedAppointment.setDoctor(appointment.getDoctor());
        updatedAppointment.setStatus(appointment.getStatus());
        updatedAppointment.setNotes(appointment.getNotes());
        updatedAppointment.setPatient(appointment.getPatient());
        return this.appointmentRepository.save(updatedAppointment);
    }

    @Override
    public Appointment completeAppointment(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + appointmentId));

        // Verify the doctor owns this appointment
        if (appointment.getDoctor().getId() != doctorId) {
            throw new RuntimeException("You can only complete your own appointments");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment bookAppointment(Long appointmentId, Long doctorId) {
        return null;
    }
}
