package org.factoriaf5.patient;
import java.util.List;

import org.factoriaf5.patient.dto.CreatePatientDTO;
import org.factoriaf5.patient.dto.PatientDTO;

public interface PatientService {
    List<PatientDTO> getPatientsByOwner(String dni);
    PatientDTO getById(Integer id);
    PatientDTO create(CreatePatientDTO dto);
    void delete(Integer id);
}
