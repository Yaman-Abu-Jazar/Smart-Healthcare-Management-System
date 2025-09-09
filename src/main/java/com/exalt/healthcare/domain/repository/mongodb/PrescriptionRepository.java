package com.exalt.healthcare.domain.repository.mongodb;

import com.exalt.healthcare.domain.model.document.Prescription;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends DocumentRepository<Prescription, Long>{
    List<Prescription> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<Prescription> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
    List<Prescription> findByPatientIdAndDoctorIdOrderByCreatedAtDesc(Long patientId, Long doctorId);
}
