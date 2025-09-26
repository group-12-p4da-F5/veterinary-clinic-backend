package org.factoriaf5.user.mapper;

import java.util.Set;

import org.factoriaf5.role.Role;
import org.factoriaf5.user.User;
import org.factoriaf5.user.UserProfile;
import org.factoriaf5.user.dtos.UserRegisterRequest;
import org.factoriaf5.user.dtos.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User toEntity(UserRegisterRequest request, String hashedPassword, Set<Role> roles) {
    User user = new User();
    user.setDni(request.dni());
    user.setPasswordHash(hashedPassword);
    user.setRoles(roles);

    UserProfile profile = UserProfile.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .user(user)
        .build();

    user.setProfile(profile);

    return user;
  }

  public UserResponse toResponse(User user) {
    String roleName = user.getRoles().stream()
        .findFirst()
        .map(Role::getName)
        .orElse(null);

    return new UserResponse(
        user.getDni(),
        user.getProfile().getFirstName(),
        user.getProfile().getLastName(),
        user.getProfile().getEmail(),
        user.getProfile().getPhoneNumber(),
        roleName);
  }
}
