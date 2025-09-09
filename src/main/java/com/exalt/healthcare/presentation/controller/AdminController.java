package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.service.implementations.AppointmentServiceImpl;
import com.exalt.healthcare.domain.service.implementations.DoctorServiceImpl;
import com.exalt.healthcare.domain.service.implementations.PatientServiceImpl;
import com.exalt.healthcare.domain.service.implementations.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DoctorServiceImpl doctorService;
    private final PatientServiceImpl patientService;
    private final UserServiceImpl userService;
    private final AppointmentServiceImpl appointmentService;

    @Autowired
    public AdminController(DoctorServiceImpl doctorService, PatientServiceImpl patientService, UserServiceImpl userService, AppointmentServiceImpl appointmentService){
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }
    /// //////////////// Doctors Management
    @PostMapping("/doctor/add")
    public Doctor addNewDoctor(@Valid @RequestBody Doctor doctor){
        return this.doctorService.addingNewDoctor(doctor);
    }

    @PutMapping("/doctor/update/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor doctor)
            throws DoctorNotFoundException {
        Doctor updatedDoctor = this.doctorService.updateDoctor(id, doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/doctor/delete/{id}")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable Long id) throws DoctorNotFoundException{
        this.doctorService.deleteDoctorById(id);
        return ResponseEntity.ok().build();
    }
    /// /////////////////// Patient Management
    @GetMapping("/patient/all")
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> list = this.patientService.getAllPatients();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/patient/add")
    public ResponseEntity<Patient> addNewPatient(@Valid @RequestBody Patient patient){
        return ResponseEntity.ok(this.patientService.savePatient(patient));
    }

    @GetMapping("/patient/get/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(this.patientService.getPatientById(id));
    }

    @PutMapping("/patient/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        Patient updatedPatient = this.patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/patient/delete/{id}")
    public ResponseEntity<Patient> deletePatient(@PathVariable Long id){
        this.patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
    /// ///////////////// User Management
    @PostMapping("/user/add")
    public User addNewUser(@Valid @RequestBody User user){
        return this.userService.createUser(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<User> addNewUser(@PathVariable Long id){
        this.userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(this.userService.findAllUsers());
    }

    @GetMapping("/user/get/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(this.userService.findByEmail(email));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user){
        return ResponseEntity.ok(this.userService.updateUser(id, user));
    }
    /// /////////////////// Appointment Management
    @GetMapping("/appointment/get/all")
    public List<Appointment> getAllAppointments(){
        return this.appointmentService.getAllAppointments();
    }

    @PostMapping("/appointment/add")
    public Appointment addNewAppointment(@Valid @RequestBody Appointment appointment){
        return this.appointmentService.addNewAppointment(appointment);
    }

    @DeleteMapping("/appointment/delete/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable Long id){
        this.appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/appointment/update/{id}")
    public ResponseEntity<Appointment> updateAppointment(@Valid @RequestBody Appointment newAppointment, @PathVariable Long id)
            throws AppointmentNotFoundException {
        Appointment appointment = this.appointmentService.updateAppointment(id, newAppointment);
        return ResponseEntity.ok(appointment);
    }
}
