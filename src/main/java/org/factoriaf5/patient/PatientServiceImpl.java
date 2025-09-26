package org.factoriaf5.patient;

import org.factoriaf5.patient.dto.CreatePatientDTO;
import org.factoriaf5.patient.dto.PatientDTO;
import org.factoriaf5.user.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

  private final PatientRepository repository;
  private final UserRepository userRepository;

  public PatientServiceImpl(PatientRepository repository, UserRepository userRepository) {
    this.repository = repository;
    this.userRepository = userRepository;
  }

  @Override
  public List<PatientDTO> getPatientsByOwner(String dni) {
    return repository.findByManagerDni(dni).stream()
        .map(PatientMapper::toDTO)
        .toList();
  }

  @Override
  public PatientDTO getById(Integer id) {
    return repository.findById(id)
        .map(PatientMapper::toDTO)
        .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
  }

  @Override
  public PatientDTO create(CreatePatientDTO dto) {
    var owner = userRepository.findById(dto.getOwnerDni())
        .orElseThrow(() -> new IllegalArgumentException("Dueño no encontrado"));
    var entity = PatientMapper.toEntity(dto, owner);
    return PatientMapper.toDTO(repository.save(entity));
  }

  @Override
  public void delete(Integer id) {
    repository.deleteById(id);
  }
}
