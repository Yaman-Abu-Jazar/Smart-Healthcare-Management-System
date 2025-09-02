package com.exalt.healthcare.domain.repository.jpa;

import com.exalt.healthcare.domain.model.entity.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends EntityRepository<Doctor, Long> {
}
