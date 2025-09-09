package com.exalt.healthcare.presentation.controller;

import com.exalt.healthcare.common.exception.AppointmentNotFoundException;
import com.exalt.healthcare.common.payload.AppointmentDto;
import com.exalt.healthcare.common.payload.MedicalRecordDto;
import com.exalt.healthcare.common.payload.PrescriptionDto;
import com.exalt.healthcare.domain.model.document.MedicalRecord;
import com.exalt.healthcare.domain.model.document.Prescription;
import com.exalt.healthcare.domain.model.entity.Appointment;
import com.exalt.healthcare.domain.service.implementations.AppointmentServiceImpl;
import com.exalt.healthcare.domain.service.implementations.MedicalRecordServiceImpl;
import com.exalt.healthcare.domain.service.implementations.PrescriptionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final MedicalRecordServiceImpl medicalRecordService;
    private final PrescriptionServiceImpl prescriptionService;
    private final AppointmentServiceImpl appointmentService;

    @Autowired
    public DoctorController(MedicalRecordServiceImpl medicalRecordService, PrescriptionServiceImpl prescriptionService, AppointmentServiceImpl appointmentService){
        this.medicalRecordService = medicalRecordService;
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
    }
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////// Prescription Management
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/prescription/add")
    public ResponseEntity<Prescription> addNewPrescription(@Valid @RequestBody PrescriptionDto prescription){
        return ResponseEntity.ok(this.prescriptionService.savePrescription(prescription));
    }

    @PutMapping("/prescription/update/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @Valid @RequestBody Prescription prescription){
        return ResponseEntity.ok(this.prescriptionService.updatePrescription(id, prescription));
    }

    @DeleteMapping("/prescription/delete/{id}")
    public ResponseEntity<Prescription> deletePrescription(@PathVariable Long id){
        this.prescriptionService.deletePrescription(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/prescription/patient/{id}")
    public ResponseEntity<List<Prescription>> getPrescriptionsForPatient(@PathVariable Long id){
        return ResponseEntity.ok(this.prescriptionService.getPrescriptionsByPatientId(id));
    }

    @GetMapping("/prescription/all")
    public ResponseEntity<List<Prescription>> getMyPrescriptions(){
        return ResponseEntity.ok(this.prescriptionService.getPrescriptionsByDoctorId());
    }
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////// Appointment Management
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/appointment/get/all")
    public List<Appointment> getAllAppointments(){
        return this.appointmentService.getAllAppointments();
    }

    @PostMapping("/appointment/add")
    public Appointment addNewAppointment(@Valid @RequestBody AppointmentDto appointment){
        return this.appointmentService.addNewAppointment(appointment);
    }

    @DeleteMapping("/appointment/delete/{id}")
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable Long id){
        this.appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/appointment/update/{id}")
    public ResponseEntity<Appointment> updateAppointment(@Valid @RequestBody Appointment newAppointment, @PathVariable Long id)
            throws AppointmentNotFoundException {
        Appointment appointment = this.appointmentService.updateAppointment(id, newAppointment);
        return ResponseEntity.ok(appointment);
    }
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////// Medical Record Management
    /// ////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/record/{id}")
    public ResponseEntity<MedicalRecord> getRecordById(@PathVariable Long id){
        return ResponseEntity.ok(this.medicalRecordService.getMedicalRecordById(id));
    }

    @PostMapping("/record/add")
    public ResponseEntity<MedicalRecord> addNewRecord(@Valid @RequestBody MedicalRecordDto record){
        return ResponseEntity.ok(this.medicalRecordService.saveMedicalRecord(record));
    }

    @PutMapping("/record/update/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecord record){
        return ResponseEntity.ok(this.medicalRecordService.updateMedicalRecord(id, record));
    }

    @DeleteMapping("/record/delete/{id}")
    public ResponseEntity<MedicalRecord> deleteMedicalRecord(@PathVariable Long id){
        this.medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.ok().build();
    }
}
