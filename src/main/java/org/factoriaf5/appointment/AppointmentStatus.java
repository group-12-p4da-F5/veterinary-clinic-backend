package org.factoriaf5.appointment;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    PENDING("Pendiente"),
    ATTENDED("Atendido"),
    MISSED("No asistido");

    private final String label;

    AppointmentStatus(String label) {
        this.label = label;
    }
}
