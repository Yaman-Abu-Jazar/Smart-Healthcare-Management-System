package com.exalt.healthcare.domain.repository.mongodb;

import com.exalt.healthcare.domain.model.document.Prescription;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends DocumentRepository<Prescription, Long>{
}
