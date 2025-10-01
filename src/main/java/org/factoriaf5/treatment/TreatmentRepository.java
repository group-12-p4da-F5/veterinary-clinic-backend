package org.factoriaf5.treatment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {
    List<Treatment> findByPatientPatientId(Integer patientId);
}
