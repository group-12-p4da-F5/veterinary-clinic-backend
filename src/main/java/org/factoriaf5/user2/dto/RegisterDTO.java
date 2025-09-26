package org.factoriaf5.user2.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String dni; // Solo lo pide el admin al crear usuarios
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password; // Solo al registrarse
}
