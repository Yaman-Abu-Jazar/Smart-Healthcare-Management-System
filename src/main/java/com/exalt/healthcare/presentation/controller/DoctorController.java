package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.document.Prescription;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.service.implementations.AppointmentServiceImpl;
import com.exalt.healthcare.domain.service.implementations.DoctorServiceImpl;
import com.exalt.healthcare.domain.service.implementations.PrescriptionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorServiceImpl doctorService;
    private final PrescriptionServiceImpl prescriptionService;
    private final AppointmentServiceImpl appointmentService;

    @Autowired
    public DoctorController(DoctorServiceImpl doctorService, PrescriptionServiceImpl prescriptionService, AppointmentServiceImpl appointmentService){
        this.doctorService = doctorService;
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/prescription/add")
    public ResponseEntity<Prescription> addNewPrescription(@Valid @RequestBody Prescription prescription){
        return ResponseEntity.ok(this.prescriptionService.savePrescription(prescription));
    }

    @PutMapping("/prescription/update/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @Valid @RequestBody Prescription prescription){
        return ResponseEntity.ok(this.prescriptionService.updatePrescription(id, prescription));
    }

    @DeleteMapping("/prescription/delete/{id}")
    public ResponseEntity<Prescription> deletePrescription(@PathVariable Long id){
        this.prescriptionService.deletePrescription(id);
        return ResponseEntity.ok().build();
    }
}
