package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.service.interfaces.DoctorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    @Override
    @CachePut(value = "doctors", key = "#doctor.id")
    public Doctor addingNewDoctor(Doctor doctor){
        return this.doctorRepository.save(doctor);
    }

    @Override
    @Cacheable(value = "doctorsBySpecialty", key = "#specialty")
    public List<Doctor> findDoctorsBySpecialty(String specialty) throws DoctorNotFoundException {
        return this.doctorRepository.findDoctorBySpecialty(specialty);
    }

    @Override
    @Cacheable(value = "doctors", key = "#id")
    public Doctor findDoctorById(long id) {
        return this.doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    @Cacheable(value = "allDoctors")
    public List<Doctor> findAllDoctors() {
        return this.doctorRepository.findAll();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "doctors", key = "#id"),
            @CacheEvict(value = "allDoctors", allEntries = true)
    })
    public void deleteDoctorById(Long id) throws DoctorNotFoundException {
        Doctor doctor = this.doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        this.doctorRepository.deleteById(id);
    }
}
