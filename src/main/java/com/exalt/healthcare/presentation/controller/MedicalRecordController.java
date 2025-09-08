package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.domain.model.document.MedicalRecord;
import com.exalt.healthcare.domain.service.implementations.MedicalRecordServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class MedicalRecordController {

    private final MedicalRecordServiceImpl service;

    @Autowired
    public MedicalRecordController(MedicalRecordServiceImpl service){
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicalRecord>> getAllRecords(){
        return ResponseEntity.ok(this.service.getAllMedicalRecords());
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<MedicalRecord>> getAllRecordsByDoctorId(@PathVariable Long id){
        return ResponseEntity.ok(this.service.getMedicalRecordsByDoctorId(id));
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<MedicalRecord>> getAllRecordsByPatientId(@PathVariable Long id){
        return ResponseEntity.ok(this.service.getMedicalRecordsByPatientId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getRecordById(@PathVariable Long id){
        return ResponseEntity.ok(this.service.getMedicalRecordById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<MedicalRecord> addNewRecord(@Valid @RequestBody MedicalRecord record){
        return ResponseEntity.ok(this.service.saveMedicalRecord(record));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecord record){
        return ResponseEntity.ok(this.service.updateMedicalRecord(id, record));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MedicalRecord> deleteMedicalRecord(@PathVariable Long id){
        this.service.deleteMedicalRecord(id);
        return ResponseEntity.ok().build();
    }
}
