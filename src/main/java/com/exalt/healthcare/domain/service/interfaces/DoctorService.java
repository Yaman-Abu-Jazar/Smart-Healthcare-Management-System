package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.domain.model.entity.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor addingNewDoctor(Doctor doctor);
    List<Doctor> findDoctorsBySpecialty(String specialty);
}
