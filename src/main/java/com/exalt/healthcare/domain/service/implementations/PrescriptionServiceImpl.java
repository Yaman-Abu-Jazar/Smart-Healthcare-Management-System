package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.DoctorNotFoundException;
import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.common.exception.PrescriptionNotFoundException;
import com.exalt.healthcare.common.exception.UserNotFoundException;
import com.exalt.healthcare.common.payload.PrescriptionDto;
import com.exalt.healthcare.domain.model.document.Prescription;
import com.exalt.healthcare.domain.model.entity.Doctor;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.model.entity.User;
import com.exalt.healthcare.domain.repository.jpa.DoctorRepository;
import com.exalt.healthcare.domain.repository.jpa.PatientRepository;
import com.exalt.healthcare.domain.repository.jpa.UserRepository;
import com.exalt.healthcare.domain.repository.mongodb.PrescriptionRepository;
import com.exalt.healthcare.domain.service.interfaces.PrescriptionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;


    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, DoctorRepository doctorRepository, UserRepository userRepository, PatientRepository patientRepository){
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Prescription savePrescription(PrescriptionDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Doctor doctor = this.doctorRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with user id : " + user.getId()));

        Prescription prescription = Prescription.builder()
                .notes(dto.getNotes())
                .patientId(dto.getPatientId())
                .createdAt(LocalDateTime.now())
                .medications(dto.getMedications())
                .doctorId(doctor.getId())
                .build();
        return this.prescriptionRepository.save(prescription);
    }

    @Override
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Doctor doctor = this.doctorRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with user id : " + user.getId()));

        return this.prescriptionRepository.findByPatientIdAndDoctorIdOrderByCreatedAtDesc(patientId, doctor.getId());
    }

    @Override
    public List<Prescription> getPrescriptionsByDoctorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Doctor doctor = this.doctorRepository.findByUser(user)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with user id : " + user.getId()));

        return this.prescriptionRepository.findByDoctorIdOrderByCreatedAtDesc(doctor.getId());
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        return this.prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with id : " + id));
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return this.prescriptionRepository.findAll();
    }

    @Override
    public Prescription updatePrescription(Long id, Prescription prescription) {
        Prescription updatedPrescription = this.prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with id : " + id));

        updatedPrescription.setCreatedAt(prescription.getCreatedAt());
        updatedPrescription.setNotes(prescription.getNotes());
        updatedPrescription.setMedications(prescription.getMedications());
        updatedPrescription.setPatientId(prescription.getPatientId());
        updatedPrescription.setDoctorId(prescription.getDoctorId());

        return this.prescriptionRepository.save(updatedPrescription);
    }

    @Override
    public void deletePrescription(Long id) {
        Prescription updatedPrescription = this.prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with id : " + id));

        this.prescriptionRepository.deleteById(id);
    }

    @Override
    public List<Prescription> getMyPatientPrescriptions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        Patient patient = this.patientRepository.findByUser(user)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with user id : " + user.getId()));

        return this.prescriptionRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId());
    }
}
