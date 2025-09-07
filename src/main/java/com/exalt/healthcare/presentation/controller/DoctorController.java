package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.service.implementations.DoctorServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorServiceImpl service;

    @Autowired
    public DoctorController(DoctorServiceImpl service){
        this.service = service;
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<Doctor>> getDoctorBySpecialty(@PathVariable String specialty) throws DoctorNotFoundException {
        List<Doctor> doctors =  this.service.findDoctorsBySpecialty(specialty);
        if(doctors.isEmpty()){
            throw new DoctorNotFoundException("No doctors found for specialty: " + specialty);
        }
        return ResponseEntity.ok(doctors);
    }

    @PostMapping("/new")
    public Doctor addNewDoctor(@Valid @RequestBody Doctor doctor){
        return   this.service.addingNewDoctor(doctor);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor doctor)
            throws DoctorNotFoundException{
        Doctor updatedDoctor = this.service.updateDoctor(id, doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable Long id) throws DoctorNotFoundException{
        this.service.deleteDoctorById(id);
        return ResponseEntity.ok().build();
    }
}
