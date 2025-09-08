package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.domain.model.document.Prescription;
import com.exalt.healthcare.domain.service.implementations.PrescriptionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionServiceImpl service;

    @Autowired
    public PrescriptionController(PrescriptionServiceImpl service){
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Prescription>> getAllPrescriptions(){
        return ResponseEntity.ok(this.service.getAllPrescriptions());
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctorId(@PathVariable Long id){
        return ResponseEntity.ok(this.service.getPrescriptionsByDoctorId(id));
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatientId(@PathVariable Long id){
        return ResponseEntity.ok(this.service.getPrescriptionsByPatientId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id){
        return ResponseEntity.ok(this.service.getPrescriptionById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Prescription> addNewPrescription(@Valid @RequestBody Prescription prescription){
        return ResponseEntity.ok(this.service.savePrescription(prescription));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @Valid @RequestBody Prescription prescription){
        return ResponseEntity.ok(this.service.updatePrescription(id, prescription));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Prescription> deletePrescription(@PathVariable Long id){
        this.service.deletePrescription(id);
        return ResponseEntity.ok().build();
    }
}
