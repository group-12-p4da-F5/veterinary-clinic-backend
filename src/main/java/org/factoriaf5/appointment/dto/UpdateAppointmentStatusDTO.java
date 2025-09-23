package org.factoriaf5.appointment.dto;

import lombok.Data;
import org.factoriaf5.appointment.AppointmentStatus;

@Data
public class UpdateAppointmentStatusDTO {
    private AppointmentStatus status;
}
