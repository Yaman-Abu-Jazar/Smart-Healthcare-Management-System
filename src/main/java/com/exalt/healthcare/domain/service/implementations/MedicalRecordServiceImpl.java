package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.MedicalRecordNotFoundException;
import com.exalt.healthcare.domain.model.document.MedicalRecord;
import com.exalt.healthcare.domain.repository.mongodb.MedicalRecordsRepository;
import com.exalt.healthcare.domain.service.interfaces.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordsRepository repository;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordsRepository repository){
        this.repository = repository;
    }

    @Override
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecord.setCreatedAt(LocalDateTime.now());
        medicalRecord.setUpdatedAt(LocalDateTime.now());
        return this.repository.save(medicalRecord);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        return this.repository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByDoctorId(Long doctorId) {
        return this.repository.findByDoctorIdOrderByCreatedAtDesc(doctorId);
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical Record not found with id : " + id));
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return this.repository.findAll();
    }

    @Override
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        MedicalRecord record = this.repository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical Record not found with id : " + id));

        record.setMedications(recordDetails.getMedications());
        record.setUpdatedAt(LocalDateTime.now());
        record.setNotes(recordDetails.getNotes());
        record.setStatus(recordDetails.getStatus());
        record.setDoctorId(recordDetails.getDoctorId());
        record.setPatientId(recordDetails.getPatientId());
        record.setLabResults(recordDetails.getLabResults());

        return this.repository.save(record);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        MedicalRecord record = this.repository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical Record not found with id : " + id));

        this.repository.deleteById(id);
    }
}
