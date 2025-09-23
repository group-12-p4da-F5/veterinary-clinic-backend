package org.factoriaf5.user.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
}
