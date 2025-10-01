package org.factoriaf5.treatment;

import org.factoriaf5.treatment.dto.TreatmentDTO;
import org.factoriaf5.treatment.dto.CreateTreatmentDTO;

import java.util.List;

public interface TreatmentService {
    TreatmentDTO create(CreateTreatmentDTO dto);
    List<TreatmentDTO> getByPatient(Integer patientId);
}
