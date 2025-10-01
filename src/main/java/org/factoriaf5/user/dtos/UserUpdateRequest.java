package org.factoriaf5.user.dtos;

public record UserUpdateRequest(
    String firstName,
    String lastName,
    String email,
    String phoneNumber) {
}
