package org.factoriaf5.patient;

import org.factoriaf5.appointment.Appointment;
import org.factoriaf5.treatment.Treatment;
import org.factoriaf5.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PatientTest {

  @Test
  void shouldCreatePatientWithBuilder() {
    Patient patient = Patient.builder()
        .patientId(1)
        .name("Luna")
        .age(5)
        .breed("Labrador")
        .gender("Female")
        .build();

    assertThat(patient.getPatientId()).isEqualTo(1);
    assertThat(patient.getName()).isEqualTo("Luna");
    assertThat(patient.getAge()).isEqualTo(5);
    assertThat(patient.getBreed()).isEqualTo("Labrador");
    assertThat(patient.getGender()).isEqualTo("Female");
  }

  @Test
  void shouldAllowSettingAndGettingManager() {
    User manager = User.builder().dni("12345678A").build();
    Patient patient = new Patient();
    patient.setManager(manager);

    assertThat(patient.getManager()).isNotNull();
    assertThat(patient.getManager().getDni()).isEqualTo("12345678A");
  }

  @Test
  void shouldAllowSettingAndGettingAppointments() {
    Appointment appointment = new Appointment();
    Patient patient = new Patient();
    appointment.setPatient(patient);
    patient.setAppointments(List.of(appointment));

    assertThat(patient.getAppointments()).hasSize(1);
    assertThat(patient.getAppointments().get(0).getPatient()).isEqualTo(patient);
  }

  @Test
  void shouldAllowSettingAndGettingTreatments() {
    Treatment treatment = new Treatment();
    Patient patient = new Patient();
    treatment.setPatient(patient);
    patient.setTreatments(List.of(treatment));

    assertThat(patient.getTreatments()).hasSize(1);
    assertThat(patient.getTreatments().get(0).getPatient()).isEqualTo(patient);
  }

  @Test
  void equalsShouldReturnTrueForSameId() {
    Patient patient1 = Patient.builder().patientId(1).name("Luna").build();
    Patient patient2 = Patient.builder().patientId(1).name("Luna").build();

    assertThat(patient1).isEqualTo(patient2);
    assertThat(patient1.hashCode()).isEqualTo(patient2.hashCode());
  }

  @Test
  void equalsShouldReturnFalseForDifferentId() {
    Patient patient1 = Patient.builder().patientId(1).build();
    Patient patient2 = Patient.builder().patientId(2).build();

    assertThat(patient1).isNotEqualTo(patient2);
  }

  @Test
  void equalsShouldReturnFalseWhenIdIsNull() {
    Patient patient1 = Patient.builder().patientId(null).name("Luna").build();
    Patient patient2 = Patient.builder().patientId(1).name("Luna").build();

    assertThat(patient1).isNotEqualTo(patient2);
  }

}
