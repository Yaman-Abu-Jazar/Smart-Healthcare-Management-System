package com.exalt.healthcare.domain.repository.jpa;

import com.exalt.healthcare.domain.model.entity.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends EntityRepository<Patient, Long> {
}
