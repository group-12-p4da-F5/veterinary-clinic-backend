package org.factoriaf5.user.dtos;

public record UserRegisterRequest(
                String dni,
                String firstName,
                String lastName,
                String email,
                String phoneNumber,
                String password) {
}
