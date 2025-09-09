package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.common.exception.MedicalRecordNotFoundException;
import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.common.exception.UserNotFoundException;
import com.exalt.healthcare.common.payload.MedicalRecordDto;
import com.exalt.healthcare.domain.model.document.MedicalRecord;
import com.exalt.healthcare.domain.model.document.Prescription;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.repository.jpa.PatientRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.repository.mongodb.MedicalRecordsRepository;
import com.exalt.healthcare.domain.service.interfaces.MedicalRecordService;
import com.exalt.healthcare.domain.service.interfaces.SequenceGeneratorService;
import com.exalt.healthcare.domain.valueobject.MedicalRecordStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordsRepository medicalRecordsRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public MedicalRecordServiceImpl(SequenceGeneratorService sequenceGeneratorService,
                                    MedicalRecordsRepository medicalRecordsRepository,
                                    DoctorRepository doctorRepository,
                                    UserRepository userRepository,
                                    PatientRepository patientRepository){
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.medicalRecordsRepository = medicalRecordsRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public MedicalRecord saveMedicalRecord(MedicalRecordDto medicalRecord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Doctor doctor = this.doctorRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with user id : " + user.getId()));

        MedicalRecord record = MedicalRecord.builder()
                .id(sequenceGeneratorService.generateSequence(MedicalRecord.SEQUENCE_NAME))
                .createdAt(LocalDateTime.now())
                .notes(medicalRecord.getNotes())
                .patientId(medicalRecord.getPatientId())
                .medications(medicalRecord.getMedications())
                .status(MedicalRecordStatus.ACTIVE)
                .doctorId(doctor.getId())
                .labResults(medicalRecord.getLabResults())
                .updatedAt(LocalDateTime.now())
                .build();

        return this.medicalRecordsRepository.save(record);
    }

    @Override
    public List<MedicalRecord> getMyMedicalRecordsByPatient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Patient patient = this.patientRepository.findByUser(user)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with user id : " + user.getId()));

        return this.medicalRecordsRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId());
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByDoctorId(Long doctorId) {
        return this.medicalRecordsRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId);
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        return this.medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical Record not found with id : " + id));
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return this.medicalRecordsRepository.findAll();
    }

    @Override
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        MedicalRecord record = this.medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical Record not found with id : " + id));

        record.setMedications(recordDetails.getMedications());
        record.setUpdatedAt(LocalDateTime.now());
        record.setNotes(recordDetails.getNotes());
        record.setStatus(recordDetails.getStatus());
        record.setDoctorId(recordDetails.getDoctorId());
        record.setPatientId(recordDetails.getPatientId());
        record.setLabResults(recordDetails.getLabResults());

        return this.medicalRecordsRepository.save(record);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        MedicalRecord record = this.medicalRecordsRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical Record not found with id : " + id));

        this.medicalRecordsRepository.deleteById(id);
    }
}
