package org.factoriaf5.treatment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;

    @Override
    public List<Treatment> findAll() {
        return treatmentRepository.findAll();
    }

    @Override
    public Optional<Treatment> findById(Integer id) {
        return treatmentRepository.findById(id);
    }

    @Override
    public Treatment save(Treatment treatment) {
        return treatmentRepository.save(treatment);
    }

    @Override
    public Treatment update(Integer id, Treatment treatment) {
        return treatmentRepository.findById(id)
                .map(existing -> {
                    treatment.setTreatmentId(id);
                    return treatmentRepository.save(treatment);
                })
                .orElseThrow(() -> new RuntimeException("Treatment not found with id " + id));
    }

    @Override
    public void delete(Integer id) {
        treatmentRepository.deleteById(id);
    }
}
