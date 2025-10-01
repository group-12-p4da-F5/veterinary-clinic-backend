package org.factoriaf5.appointment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateAppointmentDTO {
    private LocalDateTime dateTime; // null en caso de urgencia
    private String type;
    private String reason;
    private Integer patientId;
}
