package org.factoriaf5.treatment;

import org.factoriaf5.treatment.dto.TreatmentDTO;
import org.factoriaf5.treatment.dto.CreateTreatmentDTO;
import org.factoriaf5.patient.Patient;

public class TreatmentMapper {

    public static TreatmentDTO toDTO(Treatment entity) {
        TreatmentDTO dto = new TreatmentDTO();
        dto.setTreatmentId(entity.getTreatmentId());
        dto.setDescription(entity.getDescription());
        dto.setTreatmentDate(entity.getTreatmentDate());
        dto.setPatientId(entity.getPatient().getPatientId());
        return dto;
    }

    public static Treatment toEntity(CreateTreatmentDTO dto, Patient patient) {
        return Treatment.builder()
                .description(dto.getDescription())
                .treatmentDate(dto.getTreatmentDate())
                .patient(patient)
                .build();
    }
}
