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
        return ResponseEntity.ok(this.service.getPrescriptionsByDoctorId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id){
        return ResponseEntity.ok(this.service.getPrescriptionById(id));
    }
}
