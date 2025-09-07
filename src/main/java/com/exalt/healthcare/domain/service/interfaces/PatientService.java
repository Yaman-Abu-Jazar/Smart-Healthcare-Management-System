package com.exalt.healthcare.domain.service.interfaces;

import com.exalt.healthcare.domain.model.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient savePatient(Patient patient);
    List<Patient> getAllPatients();
    Patient getPatientById(Long id);
//    Optional<Patient> getPatientByUserId(Long userId);
    Patient updatePatient(Long id, Patient patientDetails);
    void deletePatient(Long id);
}
