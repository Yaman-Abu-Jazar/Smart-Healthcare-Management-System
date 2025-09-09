package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.service.interfaces.DoctorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, UserRepository userRepository){
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Doctor addingNewDoctor(Doctor doctor){
        return this.doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> findDoctorsBySpecialty(String specialty) throws DoctorNotFoundException {
        return this.doctorRepository.findDoctorBySpecialty(specialty);
    }

    @Override
    public Doctor findDoctorById(long id) {
        return this.doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return this.doctorRepository.findAll();
    }

    @Override
    public void deleteDoctorById(Long id) throws DoctorNotFoundException {
        Doctor doctor = this.doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        this.doctorRepository.deleteById(id);
    }

}
