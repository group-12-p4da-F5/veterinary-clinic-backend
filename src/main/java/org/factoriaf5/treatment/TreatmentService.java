package org.factoriaf5.treatment;

import java.util.List;
import java.util.Optional;

public interface TreatmentService {
    List<Treatment> findAll();
    Optional<Treatment> findById(Integer id);
    Treatment save(Treatment treatment);
    Treatment update(Integer id, Treatment treatment);
    void delete(Integer id);
}
