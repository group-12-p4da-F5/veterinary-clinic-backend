package org.factoriaf5.patient.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Integer patientId;
    private String name;
    private int age;
    private String breed;
    private String gender;
    private String ownerDni;
}
