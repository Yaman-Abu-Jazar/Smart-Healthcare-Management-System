package com.exalt.healthcare.domain.repository.mongodb;


import com.exalt.healthcare.domain.model.document.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordsRepository extends DocumentRepository<MedicalRecord, Long>{
    List<MedicalRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<MedicalRecord> findByDoctorIdOrderByCreatedAtDesc(Long patientId);
}
