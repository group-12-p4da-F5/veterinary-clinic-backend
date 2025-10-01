package org.factoriaf5.treatment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.factoriaf5.patient.Patient;
import org.factoriaf5.patient.PatientRepository;
import org.factoriaf5.treatment.dto.CreateTreatmentDTO;
import org.factoriaf5.treatment.dto.TreatmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TreatmentServiceImplTest {

  private TreatmentRepository treatmentRepository;
  private PatientRepository patientRepository;
  private TreatmentServiceImpl treatmentService;

  @BeforeEach
  void setUp() {
    treatmentRepository = mock(TreatmentRepository.class);
    patientRepository = mock(PatientRepository.class);
    treatmentService = new TreatmentServiceImpl(treatmentRepository, patientRepository);
  }

  @Test
  void createShouldSaveAndReturnDto() {
    CreateTreatmentDTO dto = new CreateTreatmentDTO();
    dto.setDescription("Vacuna antirrábica");
    dto.setTreatmentDate(LocalDateTime.now());
    dto.setPatientId(1);

    Patient patient = new Patient();
    Treatment treatment = new Treatment();
    Treatment savedTreatment = new Treatment();

    TreatmentDTO mappedDto = new TreatmentDTO();
    mappedDto.setTreatmentId(1);
    mappedDto.setDescription("Vacuna antirrábica");
    mappedDto.setPatientId(1);

    when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
    when(treatmentRepository.save(any(Treatment.class))).thenReturn(savedTreatment);

    try (var mocked = mockStatic(TreatmentMapper.class)) {
      mocked.when(() -> TreatmentMapper.toEntity(dto, patient)).thenReturn(treatment);
      mocked.when(() -> TreatmentMapper.toDTO(savedTreatment)).thenReturn(mappedDto);

      TreatmentDTO result = treatmentService.create(dto);

      assertThat(result).isEqualTo(mappedDto);
      verify(treatmentRepository).save(treatment);
    }
  }

  @Test
  void createShouldThrowWhenPatientNotFound() {
    CreateTreatmentDTO dto = new CreateTreatmentDTO();
    dto.setPatientId(99);

    when(patientRepository.findById(99)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> treatmentService.create(dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Paciente no encontrado");
  }

  @Test
  void getByPatientShouldReturnDtos() {
    Treatment treatment = new Treatment();
    TreatmentDTO dto = new TreatmentDTO();
    dto.setTreatmentId(1);
    dto.setDescription("Antibiótico");

    when(treatmentRepository.findByPatientPatientId(1)).thenReturn(List.of(treatment));

    try (var mocked = mockStatic(TreatmentMapper.class)) {
      mocked.when(() -> TreatmentMapper.toDTO(treatment)).thenReturn(dto);

      List<TreatmentDTO> result = treatmentService.getByPatient(1);

      assertThat(result).containsExactly(dto);
      verify(treatmentRepository).findByPatientPatientId(1);
    }
  }
}
