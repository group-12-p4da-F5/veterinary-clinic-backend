package org.factoriaf5.patient;

import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();
    Patient getPatientById(Integer id);
    Patient getPatientByIdentification(String identificationNumber);
    Patient getPatientByOwnerDni(String ownerDni);
    Patient createPatient(Patient patient);
    Patient updatePatient(Integer id, Patient patient);
    void deletePatient(Integer id);
}
