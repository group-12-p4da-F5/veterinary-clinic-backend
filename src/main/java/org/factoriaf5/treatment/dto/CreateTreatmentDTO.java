package org.factoriaf5.treatment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateTreatmentDTO {
    private String description;
    private LocalDateTime treatmentDate;
    private Integer patientId;
}
