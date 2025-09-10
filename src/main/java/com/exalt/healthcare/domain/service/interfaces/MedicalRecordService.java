package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.common.payload.MedicalRecordDto;
import com.exalt.healthcare.domain.model.document.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecord saveMedicalRecord(MedicalRecordDto medicalRecord);
    List<MedicalRecord> getMyMedicalRecordsByPatient();
    MedicalRecord getMedicalRecordById(Long id);
    MedicalRecord updateMedicalRecord(Long id, MedicalRecord recordDetails);
    void deleteMedicalRecord(Long id);
}
