package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.common.payload.AuthenticationRequest;
import com.exalt.healthcare.common.payload.AuthenticationResponse;
import com.exalt.healthcare.domain.model.document.MedicalRecord;
import com.exalt.healthcare.domain.model.document.Prescription;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.service.implementations.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientServiceImpl patientService;
    private final DoctorServiceImpl doctorService;
    private final MedicalRecordServiceImpl recordService;
    private final AppointmentServiceImpl appointmentService;
    private final PrescriptionServiceImpl prescriptionService;

    @Autowired
    public PatientController(PatientServiceImpl patientService, DoctorServiceImpl doctorService, MedicalRecordServiceImpl recordService, AppointmentServiceImpl appointmentService, PrescriptionServiceImpl prescriptionService){
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.recordService = recordService;
        this.appointmentService = appointmentService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/doctor/specialty/{specialty}")
    public ResponseEntity<List<Doctor>> getDoctorBySpecialty(@PathVariable String specialty) throws DoctorNotFoundException {
        List<Doctor> doctors =  this.doctorService.findDoctorsBySpecialty(specialty);
        if(doctors.isEmpty()){
            throw new DoctorNotFoundException("No doctors found for specialty: " + specialty);
        }
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/patient/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        Patient updatedPatient = this.patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @GetMapping("/records/patient/{id}")
    public ResponseEntity<List<MedicalRecord>> getAllRecordsByPatientId(@PathVariable Long id){
        return ResponseEntity.ok(this.recordService.getMedicalRecordsByPatientId(id));
    }

    @GetMapping("/prescription/patient/{id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatientId(@PathVariable Long id){
        return ResponseEntity.ok(this.prescriptionService.getPrescriptionsByPatientId(id));
    }
}
