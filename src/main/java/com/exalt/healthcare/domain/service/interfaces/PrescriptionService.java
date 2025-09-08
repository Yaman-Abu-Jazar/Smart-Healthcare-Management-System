package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.domain.model.document.Prescription;

import java.util.List;

public interface PrescriptionService {
    Prescription savePrescription(Prescription prescription);
    List<Prescription> getPrescriptionsByPatientId(Long patientId);
    List<Prescription> getPrescriptionsByDoctorId(Long doctorId);
    Prescription getPrescriptionById(Long id);
    List<Prescription> getAllPrescriptions();
    Prescription updatePrescription(Long id, Prescription prescription);
    void deletePrescription(Long id);
}
