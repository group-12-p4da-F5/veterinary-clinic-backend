package org.factoriaf5.appointment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Integer appointmentId;
    private LocalDateTime dateTime;
    private String type;
    private String reason;
    private String status;
    private Integer patientId;
}
