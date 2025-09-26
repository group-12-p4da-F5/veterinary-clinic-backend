package org.factoriaf5.user2.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
