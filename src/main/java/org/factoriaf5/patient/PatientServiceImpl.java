package org.factoriaf5.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Patient> getAllPatients() {
        return repository.findAll();
    }

    @Override
    public Patient getPatientById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }

    @Override
    public Patient getPatientByIdentification(String identificationNumber) {
        return repository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado por identificación"));
    }

    @Override
    public Patient getPatientByOwnerDni(String ownerDni) {
        return repository.findByOwnerDni(ownerDni)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado por DNI del tutor"));
    }

    @Override
    public Patient createPatient(Patient patient) {
        return repository.save(patient);
    }

    @Override
    public Patient updatePatient(Integer id, Patient patient) {
        Patient existing = getPatientById(id);

        existing.setName(patient.getName());
        existing.setAge(patient.getAge());
        existing.setBreed(patient.getBreed());
        existing.setGender(patient.getGender());
        existing.setIdentificationNumber(patient.getIdentificationNumber());
        existing.setOwnerFirstName(patient.getOwnerFirstName());
        existing.setOwnerLastName(patient.getOwnerLastName());
        existing.setOwnerDni(patient.getOwnerDni());
        existing.setOwnerPhone(patient.getOwnerPhone());

        return repository.save(existing);
    }

    @Override
    public void deletePatient(Integer id) {
        repository.deleteById(id);
    }
}
