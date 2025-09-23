package org.factoriaf5.treatment;

import org.factoriaf5.treatment.dto.TreatmentDTO;
import org.factoriaf5.treatment.dto.CreateTreatmentDTO;
import org.factoriaf5.patient.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository repository;
    private final PatientRepository patientRepository;

    public TreatmentServiceImpl(TreatmentRepository repository, PatientRepository patientRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
    }

    @Override
    public TreatmentDTO create(CreateTreatmentDTO dto) {
        var patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        var entity = TreatmentMapper.toEntity(dto, patient);
        return TreatmentMapper.toDTO(repository.save(entity));
    }

    @Override
    public List<TreatmentDTO> getByPatient(Integer patientId) {
        return repository.findByPatientPatientId(patientId).stream()
                .map(TreatmentMapper::toDTO)
                .toList();
    }
}
