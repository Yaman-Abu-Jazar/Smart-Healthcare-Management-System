package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.service.interfaces.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository repository){
        this.repository = repository;
    }

    @Override
    public Doctor addingNewDoctor(Doctor doctor){
        return this.repository.save(doctor);
    }

    @Override
    public List<Doctor> findDoctorsBySpecialty(String specialty) throws DoctorNotFoundException {
        return this.repository.findDoctorBySpecialty(specialty);
    }

}
