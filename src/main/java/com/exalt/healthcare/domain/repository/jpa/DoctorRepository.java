package com.exalt.healthcare.domain.repository.jpa;

import com.exalt.healthcare.domain.model.entity.Doctor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends EntityRepository<Doctor, Long> {
    List<Doctor> findDoctorBySpecialty(String Specialty);
}
