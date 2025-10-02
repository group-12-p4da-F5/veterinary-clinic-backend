package org.factoriaf5.appointment;

import org.factoriaf5.patient.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentTest {

  @Test
  void shouldCreateAppointmentWithBuilder() {
    LocalDateTime now = LocalDateTime.now();

    Appointment appointment = Appointment.builder()
        .appointmentId(1)
        .dateTime(now)
        .type(AppointmentType.STANDARD)
        .reason("Annual health check")
        .status(AppointmentStatus.PENDING)
        .build();

    assertThat(appointment.getAppointmentId()).isEqualTo(1);
    assertThat(appointment.getDateTime()).isEqualTo(now);
    assertThat(appointment.getType()).isEqualTo(AppointmentType.STANDARD);
    assertThat(appointment.getReason()).isEqualTo("Annual health check");
    assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.PENDING);
  }

  @Test
  void shouldAllowSettingAndGettingPatient() {
    Patient patient = Patient.builder().patientId(10).name("Luna").build();

    Appointment appointment = new Appointment();
    appointment.setPatient(patient);

    assertThat(appointment.getPatient()).isNotNull();
    assertThat(appointment.getPatient().getName()).isEqualTo("Luna");
  }

  @Test
  void equalsShouldReturnTrueForSameId() {
    Appointment appointment1 = Appointment.builder().appointmentId(1).build();
    Appointment appointment2 = Appointment.builder().appointmentId(1).build();

    assertThat(appointment1).isEqualTo(appointment2);
    assertThat(appointment1.hashCode()).isEqualTo(appointment2.hashCode());
  }

  @Test
  void equalsShouldReturnFalseForDifferentIds() {
    Appointment appointment1 = Appointment.builder().appointmentId(1).build();
    Appointment appointment2 = Appointment.builder().appointmentId(2).build();

    assertThat(appointment1).isNotEqualTo(appointment2);
  }

  @Test
  void equalsShouldReturnFalseWhenIdIsNull() {
    Appointment appointment1 = Appointment.builder().appointmentId(null).build();
    Appointment appointment2 = Appointment.builder().appointmentId(1).build();

    assertThat(appointment1).isNotEqualTo(appointment2);
  }

}
