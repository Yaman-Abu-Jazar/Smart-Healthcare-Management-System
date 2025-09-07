package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.service.implementations.PatientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientServiceImpl service;

    @Autowired
    public PatientController(PatientServiceImpl service){
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> list = this.service.getAllPatients();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<Patient> addNewPatient(@Valid @RequestBody Patient patient){
        return ResponseEntity.ok(this.service.savePatient(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getPatientById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        Patient updatedPatient = this.service.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Patient> deletePatient(@PathVariable Long id){
        this.service.deletePatient(id);
        return ResponseEntity.ok().build();
    }
}
