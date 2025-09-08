package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
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

    @Override
    public Doctor findDoctorById(long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return this.repository.findAll();
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor updatedDoctor = this.repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));

        updatedDoctor.setFirst_name(doctorDetails.getFirst_name());
        updatedDoctor.setLast_name(doctorDetails.getLast_name());
        updatedDoctor.setSpecialty(doctorDetails.getSpecialty());
        updatedDoctor.setPhone(doctorDetails.getPhone());
        updatedDoctor.setAppointmentList(doctorDetails.getAppointmentList());

        return this.repository.save(updatedDoctor);
    }

    @Override
    public void deleteDoctorById(Long id) throws DoctorNotFoundException {
        Doctor doctor = this.repository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        this.repository.deleteById(id);
    }

}
