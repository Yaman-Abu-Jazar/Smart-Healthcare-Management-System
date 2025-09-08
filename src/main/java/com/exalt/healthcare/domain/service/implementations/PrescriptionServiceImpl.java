package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.PrescriptionNotFoundException;
import com.exalt.healthcare.domain.model.document.Prescription;
import com.exalt.healthcare.domain.repository.mongodb.PrescriptionRepository;
import com.exalt.healthcare.domain.service.interfaces.PrescriptionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository repository;

    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepository repository){
        this.repository = repository;
    }

    @Override
    public Prescription savePrescription(Prescription prescription) {
        prescription.setCreatedAt(LocalDateTime.now());
        return this.repository.save(prescription);
    }

    @Override
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        return this.repository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Override
    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        return this.repository.findByDoctorIdOrderByCreatedAtDesc(doctorId);
    }

    @Override
    public Prescription getPrescriptionById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with id : " + id));
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return this.repository.findAll();
    }

    @Override
    public Prescription updatePrescription(Long id, Prescription prescription) {
        Prescription updatedPrescription = this.repository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with id : " + id));

        updatedPrescription.setCreatedAt(prescription.getCreatedAt());
        updatedPrescription.setNotes(prescription.getNotes());
        updatedPrescription.setMedications(prescription.getMedications());
        updatedPrescription.setPatientId(prescription.getPatientId());
        updatedPrescription.setDoctorId(prescription.getDoctorId());

        return this.repository.save(updatedPrescription);
    }

    @Override
    public void deletePrescription(Long id) {
        Prescription updatedPrescription = this.repository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with id : " + id));

        this.repository.deleteById(id);
    }
}
