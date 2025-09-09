package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.common.payload.MedicalRecordDto;
import com.exalt.healthcare.domain.model.document.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecord saveMedicalRecord(MedicalRecordDto medicalRecord);
    List<MedicalRecord> getMyMedicalRecordsByPatient();
    List<MedicalRecord> getMedicalRecordsByDoctorId(Long doctorId);
    MedicalRecord getMedicalRecordById(Long id);
    List<MedicalRecord> getAllMedicalRecords();
    MedicalRecord updateMedicalRecord(Long id, MedicalRecord recordDetails);
    void deleteMedicalRecord(Long id);
}
