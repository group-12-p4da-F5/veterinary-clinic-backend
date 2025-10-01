package org.factoriaf5.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.factoriaf5.patient.dto.CreatePatientDTO;
import org.factoriaf5.patient.dto.PatientDTO;
import org.factoriaf5.user.User;
import org.factoriaf5.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class PatientServiceImplTest {

  private PatientRepository patientRepository;
  private UserRepository userRepository;
  private PatientServiceImpl patientService;

  @BeforeEach
  void setUp() {
    patientRepository = mock(PatientRepository.class);
    userRepository = mock(UserRepository.class);
    patientService = new PatientServiceImpl(patientRepository, userRepository);
  }

  @Test
  void getPatientsByOwnerShouldReturnDtos() {
    Patient patient = new Patient();
    PatientDTO dto = new PatientDTO();
    dto.setPatientId(1);
    dto.setName("Firulais");

    when(patientRepository.findByManagerDni("123")).thenReturn(List.of(patient));

    try (var mocked = mockStatic(PatientMapper.class)) {
      mocked.when(() -> PatientMapper.toDTO(patient)).thenReturn(dto);

      List<PatientDTO> result = patientService.getPatientsByOwner("123");

      assertThat(result).containsExactly(dto);
      verify(patientRepository).findByManagerDni("123");
    }
  }

  @Test
  void getByIdShouldReturnDtoWhenExists() {
    Patient patient = new Patient();
    PatientDTO dto = new PatientDTO();
    dto.setPatientId(1);
    dto.setName("Firulais");

    when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

    try (var mocked = mockStatic(PatientMapper.class)) {
      mocked.when(() -> PatientMapper.toDTO(patient)).thenReturn(dto);

      PatientDTO result = patientService.getById(1);

      assertThat(result).isEqualTo(dto);
    }
  }

  @Test
  void getByIdShouldThrowWhenNotFound() {
    when(patientRepository.findById(1)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> patientService.getById(1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Paciente no encontrado");
  }

  @Test
  void createShouldSaveAndReturnDto() {
    CreatePatientDTO dto = new CreatePatientDTO();
    dto.setName("Firulais");
    dto.setAge(3);
    dto.setGender("Macho");
    dto.setBreed("Bulldog");
    dto.setOwnerDni("123");

    User owner = User.builder().dni("123").build();
    Patient patient = new Patient();
    Patient savedPatient = new Patient();

    PatientDTO mappedDto = new PatientDTO();
    mappedDto.setPatientId(1);
    mappedDto.setName("Firulais");

    when(userRepository.findById("123")).thenReturn(Optional.of(owner));
    when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

    try (var mocked = mockStatic(PatientMapper.class)) {
      mocked.when(() -> PatientMapper.toEntity(dto, owner)).thenReturn(patient);
      mocked.when(() -> PatientMapper.toDTO(savedPatient)).thenReturn(mappedDto);

      PatientDTO result = patientService.create(dto);

      assertThat(result).isEqualTo(mappedDto);
      verify(patientRepository).save(patient);
    }
  }

  @Test
  void createShouldThrowWhenOwnerNotFound() {
    CreatePatientDTO dto = new CreatePatientDTO();
    dto.setOwnerDni("999");

    when(userRepository.findById("999")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> patientService.create(dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Dueño no encontrado");
  }

  @Test
  void deleteShouldCallRepository() {
    patientService.delete(1);

    verify(patientRepository).deleteById(1);
  }

  @Test
  void getAllPatientsShouldReturnDtos() {
    Patient patient = new Patient();
    PatientDTO dto = new PatientDTO();
    dto.setPatientId(1);
    dto.setName("Firulais");

    when(patientRepository.findAll()).thenReturn(List.of(patient));

    try (var mocked = mockStatic(PatientMapper.class)) {
      mocked.when(() -> PatientMapper.toDTO(patient)).thenReturn(dto);

      List<PatientDTO> result = patientService.getAllPatients();

      assertThat(result).containsExactly(dto);
      verify(patientRepository).findAll();
    }
  }

  @Test
  void updateShouldModifyPatientAndReturnDto() {
    CreatePatientDTO dto = new CreatePatientDTO();
    dto.setName("Bobby");
    dto.setAge(4);
    dto.setBreed("Boxer");
    dto.setGender("Macho");
    dto.setOwnerDni("123");

    User owner = User.builder().dni("123").build();
    Patient existing = new Patient();
    Patient updated = new Patient();

    PatientDTO mappedDto = new PatientDTO();
    mappedDto.setPatientId(1);
    mappedDto.setName("Bobby");

    when(userRepository.findById("123")).thenReturn(Optional.of(owner));
    when(patientRepository.findById(1)).thenReturn(Optional.of(existing));
    when(patientRepository.save(existing)).thenReturn(updated);

    try (var mocked = mockStatic(PatientMapper.class)) {
      mocked.when(() -> PatientMapper.toDTO(updated)).thenReturn(mappedDto);

      PatientDTO result = patientService.update(1, dto);

      assertThat(result).isEqualTo(mappedDto);
      assertThat(existing.getName()).isEqualTo("Bobby");
      verify(patientRepository).save(existing);
    }
  }

  @Test
  void updateShouldThrowWhenPatientNotFound() {
    CreatePatientDTO dto = new CreatePatientDTO();
    dto.setOwnerDni("123");

    when(userRepository.findById("123")).thenReturn(Optional.of(User.builder().dni("123").build()));
    when(patientRepository.findById(1)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> patientService.update(1, dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Paciente no encontrado");
  }

  @Test
  void updateShouldThrowWhenOwnerNotFound() {
    CreatePatientDTO dto = new CreatePatientDTO();
    dto.setOwnerDni("999");

    when(userRepository.findById("999")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> patientService.update(1, dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Dueño no encontrado");
  }
}
