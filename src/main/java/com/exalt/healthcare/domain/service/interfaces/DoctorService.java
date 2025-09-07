package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.domain.model.entity.Doctor;

import java.util.List;

public interface DoctorService {
    Doctor addingNewDoctor(Doctor doctor);
    List<Doctor> findDoctorsBySpecialty(String specialty);
    Doctor findDoctorById(long id);
    List<Doctor> findAllDoctors();
    Doctor updateDoctor(Long id, Doctor doctor);
    void deleteDoctorById(Long id);
}
