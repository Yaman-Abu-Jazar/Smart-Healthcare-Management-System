package com.exalt.healthcare.domain.service.implementations;

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
import com.exalt.healthcare.domain.service.interfaces.AppointmentService;
import com.exalt.healthcare.domain.valueobject.AppointmentStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, UserRepository userRepository){
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
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

        List<Appointment> appointments = this.appointmentRepository.findAppointmentsByDoctor_Id(doctor.getId());

        for (Appointment ap : appointments) {
            // Only check appointments on the same date
            if (ap.getDate().isEqual(newAppointment.getDate())) {
                boolean overlaps =
                        newAppointment.getStartTime().isBefore(ap.getEndTime()) &&
                                newAppointment.getEndTime().isAfter(ap.getStartTime());

                if (overlaps) {
                    throw new RuntimeException(
                            "New appointment conflicts with existing appointment (ID: " + ap.getId() + ")");
                }
            }
        }

        if (newAppointment.getEndTime().isBefore(newAppointment.getStartTime())
                || newAppointment.getEndTime().isEqual(newAppointment.getStartTime())) {
            throw new RuntimeException("Invalid time: appointment end time must be after start time");
        }

        return this.appointmentRepository.save(newAppointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Doctor doctor = this.doctorRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with user id : " + user.getId()));

        return this.appointmentRepository.findAppointmentsByDoctor_Id(doctor.getId());
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
        return this.appointmentRepository.findAppointmentsByDoctor_Id(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return this.appointmentRepository.findAppointmentsByPatient_Id(patientId);
    }

    @Override
    public List<Appointment> getPatientAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Patient patient = this.patientRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Patient not found with user id : " + user.getId()));

        return this.appointmentRepository.findAppointmentsByPatient_Id(patient.getId());
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
        updatedAppointment.setStartTime(appointment.getStartTime());
        updatedAppointment.setEndTime(appointment.getEndTime());
        return this.appointmentRepository.save(updatedAppointment);
    }

    @Override
    public Appointment completeAppointment(Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + appointmentId));

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Doctor doctor = this.doctorRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with user id : " + user.getId()));
        // Verify the doctor owns this appointment
        if (appointment.getDoctor().getId() != doctor.getId()) {
            throw new RuntimeException("You can only complete your own appointments");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment bookAppointment(Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Appointment appointment = this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id : " + appointmentId));

        Patient patient = this.patientRepository.findByUser(user)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with user id : " + user.getId()));

        if(appointment.getStatus() == AppointmentStatus.SCHEDULED){
            List<Appointment> myAppointments = this.appointmentRepository.findAppointmentsByPatient_Id(patient.getId());
            for(Appointment ap: myAppointments){
                if(ap.getStartTime().isEqual(appointment.getStartTime()) || (ap.getStartTime().isAfter(appointment.getStartTime()) && ap.getStartTime().isBefore(appointment.getEndTime()))
                || (ap.getEndTime().isAfter(appointment.getStartTime()) && ap.getEndTime().isBefore(appointment.getEndTime()))){
                    throw new RuntimeException("Appointment with id : " + appointment.getId() + " has a contradiction with your appointments");
                }
            }
            appointment.setStatus(AppointmentStatus.BUSY);
            appointment.setPatient(patient);
            return this.appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Appointment with id : " + appointmentId + " is not available");
        }
    }

    @Override
    public Appointment cancelAppointment(Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Appointment appointment = this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id : " + appointmentId));

        Patient patient = this.patientRepository.findByUser(user)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with user id : " + user.getId()));

        if(appointment.getStatus() == AppointmentStatus.BUSY){
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointment.setPatient(null);
            return this.appointmentRepository.save(appointment);
        } else {
            throw new AppointmentNotFoundException("Appointment with id : " + appointmentId + " is available already");
        }
    }

    @Override
    public Appointment cancelAppointmentByDoctor(Long appointmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + appointmentId));

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Doctor doctor = this.doctorRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with user id : " + user.getId()));
        // Verify the doctor owns this appointment
        if (appointment.getDoctor().getId() != doctor.getId()) {
            throw new RuntimeException("You can only complete your own appointments");
        }
        if(appointment.getStatus() != AppointmentStatus.CANCELLED){
            appointment.setStatus(AppointmentStatus.CANCELLED);
            return this.appointmentRepository.save(appointment);
        } else {
            throw new AppointmentNotFoundException("Appointment with id : " + appointmentId + " has been cancelled before");
        }
    }
}
