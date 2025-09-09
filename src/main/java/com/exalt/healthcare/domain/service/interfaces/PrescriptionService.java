package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.common.payload.PrescriptionDto;
import com.exalt.healthcare.domain.model.document.Prescription;

import java.util.List;

public interface PrescriptionService {
    Prescription savePrescription(PrescriptionDto dto);
    List<Prescription> getPrescriptionsByPatientId(Long patientId);
    List<Prescription> getPrescriptionsByDoctorId();
    Prescription getPrescriptionById(Long id);
    List<Prescription> getAllPrescriptions();
    Prescription updatePrescription(Long id, Prescription prescription);
    void deletePrescription(Long id);
    List<Prescription> getMyPatientPrescriptions();
}
