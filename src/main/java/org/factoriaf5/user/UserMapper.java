package org.factoriaf5.user;

import org.factoriaf5.user.dto.*;

public class UserMapper {

    public static UserDTO toDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setDni(entity.getDni());
        if (entity.getProfile() != null) {
            dto.setFirstName(entity.getProfile().getFirstName());
            dto.setLastName(entity.getProfile().getLastName());
            dto.setEmail(entity.getProfile().getEmail());
            dto.setPhoneNumber(entity.getProfile().getPhoneNumber());
        }
        dto.setRole(entity.getRole().getName());
        return dto;
    }

    public static User toEntity(RegisterDTO dto, String passwordHash, UserRole role) {
        User user = User.builder()
                .dni(dto.getDni())
                .passwordHash(passwordHash)
                .role(role)
                .build();

        UserProfile profile = UserProfile.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .user(user)
                .build();

        user.setProfile(profile);
        return user;
    }

    public static void updateEntity(User user, UpdateUserDTO dto) {
        if (dto.getFirstName() != null) user.getProfile().setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.getProfile().setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.getProfile().setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) user.getProfile().setPhoneNumber(dto.getPhoneNumber());
    }
}
