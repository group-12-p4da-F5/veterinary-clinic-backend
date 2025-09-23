package org.factoriaf5.appointment;

import org.factoriaf5.appointment.dto.AppointmentDTO;
import org.factoriaf5.appointment.dto.CreateAppointmentDTO;
import org.factoriaf5.patient.Patient;

public class AppointmentMapper {

    public static AppointmentDTO toDTO(Appointment entity) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setAppointmentId(entity.getAppointmentId());
        dto.setDateTime(entity.getDateTime());
        dto.setType(entity.getType().name());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus().name());
        dto.setPatientId(entity.getPatient().getPatientId());
        return dto;
    }

    public static Appointment toEntity(CreateAppointmentDTO dto, Patient patient) {
        return Appointment.builder()
                .dateTime(dto.getDateTime())
                .type(AppointmentType.valueOf(dto.getType()))
                .reason(dto.getReason())
                .status(AppointmentStatus.PENDING)
                .patient(patient)
                .build();
    }
}
