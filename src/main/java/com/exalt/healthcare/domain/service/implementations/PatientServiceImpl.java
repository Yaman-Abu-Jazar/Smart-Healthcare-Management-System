package com.exalt.healthcare.domain.service.implementations;

import com.exalt.healthcare.common.exception.PatientNotFoundException;
import com.exalt.healthcare.domain.model.entity.Patient;
import com.exalt.healthcare.domain.repository.jpa.PatientRepository;
import com.exalt.healthcare.domain.service.interfaces.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    @Autowired
    public PatientServiceImpl(PatientRepository repository){
        this.repository = repository;
    }

    @Override
    public Patient savePatient(Patient patient) {
        return this.repository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return this.repository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) throws PatientNotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) throws PatientNotFoundException {
        Patient updatedPatient = this.repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

        updatedPatient.setAddress(patientDetails.getAddress());
        updatedPatient.setFirst_name(patientDetails.getFirst_name());
        updatedPatient.setLast_name(patientDetails.getLast_name());
        updatedPatient.setAppointmentList(patientDetails.getAppointmentList());
        updatedPatient.setUser(patientDetails.getUser());

        return this.repository.save(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) throws PatientNotFoundException{
        Patient patient = this.repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        this.repository.deleteById(id);
    }
}
