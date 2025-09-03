package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.service.implementations.DoctorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
