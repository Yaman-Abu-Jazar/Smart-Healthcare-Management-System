package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.service.implementations.AppointmentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentServiceImpl service;

    @Autowired
    public AppointmentController(AppointmentServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Appointment> getAllAppointments(){
        return this.service.getAllAppointments();
    }

    @PostMapping("/add")
    public Appointment addNewAppointment(@Valid @RequestBody Appointment appointment){
        return this.service.addNewAppointment(appointment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable Long id){
        this.service.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Appointment> updateAppointment(@Valid @RequestBody Appointment newAppointment, @PathVariable Long id)
        throws AppointmentNotFoundException {
        Appointment appointment = this.service.updateAppointment(id, newAppointment);
         return ResponseEntity.ok(appointment);
    }
}
