package org.factoriaf5.patient.dto;

import lombok.Data;

@Data
public class CreatePatientDTO {
    private String name;
    private int age;
    private String breed;
    private String gender;
    private String ownerDni;
}
