package org.factoriaf5.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByIdentificationNumber(String identificationNumber);
    Optional<Patient> findByOwnerDni(String ownerDni);
}
