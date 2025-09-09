package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.payload.*;
import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.service.implementations.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DoctorServiceImpl doctorService;
    private final PatientServiceImpl patientService;
    private final UserServiceImpl userService;
    private final AppointmentServiceImpl appointmentService;
    private final AuthenticationServiceImpl authService;

    @Autowired
    public AdminController(DoctorServiceImpl doctorService, PatientServiceImpl patientService, UserServiceImpl userService, AppointmentServiceImpl appointmentService, AuthenticationServiceImpl authService){
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.authService = authService;
    }
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////// Doctor Management
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/doctor/update/{id}")
    public ResponseEntity<AuthenticationResponse> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDto doctor)
            throws DoctorNotFoundException {
        return ResponseEntity.ok(this.authService.updateDoctor(id, doctor));
    }

    @DeleteMapping("/doctor/delete/{id}")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable Long id) throws DoctorNotFoundException{
        this.doctorService.deleteDoctorById(id);
        return ResponseEntity.ok().build();
    }
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////// Patient Management
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/patient/all")
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> list = this.patientService.getAllPatients();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/patient/get/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(this.patientService.getPatientById(id));
    }

    @PutMapping("/patient/update/{id}")
    public ResponseEntity<AuthenticationResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDto patient) {
        return ResponseEntity.ok(this.authService.updatePatient(id, patient));
    }

    @DeleteMapping("/patient/delete/{id}")
    public ResponseEntity<Patient> deletePatient(@PathVariable Long id){
        this.patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////// User Management
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id){
        this.userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<AuthenticationResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto user){
        return ResponseEntity.ok(this.authService.updateUser(id, user));
    }
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////// Registration Management
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/doctor/register")
    public ResponseEntity<AuthenticationResponse> registerDoctor(
            @RequestBody DoctorDto request
    ) {
        return ResponseEntity.ok(authService.registerDoctor(request));
    }

    @PostMapping("/patient/register")
    public ResponseEntity<AuthenticationResponse> registerPatient(
            @RequestBody PatientDto request
    ) {
        return ResponseEntity.ok(authService.registerPatient(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @RequestBody UserDto request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
}
