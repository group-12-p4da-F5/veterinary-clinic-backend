package org.factoriaf5.appointment.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateAppointmentDTO {
    private LocalDateTime dateTime;
    private String reason;
}
