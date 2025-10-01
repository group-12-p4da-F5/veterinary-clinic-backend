package org.factoriaf5.user.dtos;

public record UserLoginRequest(
    String dni,
    String password) {
}
