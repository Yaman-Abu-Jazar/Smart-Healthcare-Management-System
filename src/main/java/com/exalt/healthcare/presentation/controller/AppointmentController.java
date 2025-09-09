package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.service.implementations.AppointmentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentServiceImpl service;

    @Autowired
    public AppointmentController(AppointmentServiceImpl service) {
        this.service = service;
    }


}
