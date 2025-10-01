package org.factoriaf5.user.dtos;

public record UserResponse(
    String dni,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String role) {
}
