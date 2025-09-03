package com.exalt.healthcare.domain.repository.mongodb;


import com.exalt.healthcare.domain.model.document.MedicalRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordsRepository extends DocumentRepository<MedicalRecord, Long>{
}
