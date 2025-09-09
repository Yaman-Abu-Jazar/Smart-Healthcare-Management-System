package com.exalt.healthcare.domain.repository.jpa;

import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends EntityRepository<Doctor, Long> {
    List<Doctor> findDoctorBySpecialty(String Specialty);
    Optional<Doctor> findByUser(User user);
}
