package org.factoriaf5.appointment;

import lombok.Getter;

@Getter
public enum AppointmentType {
    STANDARD("Estándar"),
    EMERGENCY("Emergencia");

    private final String label;

    AppointmentType(String label) {
        this.label = label;
    }
}
