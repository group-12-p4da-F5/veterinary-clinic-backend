package org.factoriaf5.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("Administrador"),
    USER("Usuario");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }
}
