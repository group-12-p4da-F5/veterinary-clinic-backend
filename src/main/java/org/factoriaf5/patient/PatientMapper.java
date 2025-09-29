package org.factoriaf5.patient;

import org.factoriaf5.patient.dto.CreatePatientDTO;
import org.factoriaf5.patient.dto.PatientDTO;
import org.factoriaf5.user.User;
import org.factoriaf5.user.repository.*;

public class PatientMapper {

    public static PatientDTO toDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setAge(patient.getAge());
        dto.setBreed(patient.getBreed());
        dto.setGender(patient.getGender());
        dto.setOwnerDni(patient.getManager().getDni());
        return dto;
    }

    public static Patient toEntity(CreatePatientDTO dto, User owner) {
        return Patient.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .breed(dto.getBreed())
                .gender(dto.getGender())
                .manager(owner)
                .build();
    }
}
