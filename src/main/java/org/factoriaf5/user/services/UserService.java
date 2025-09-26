package org.factoriaf5.user.services;

import java.util.List;

import org.factoriaf5.user.User;
import org.factoriaf5.user.dtos.UserLoginRequest;
import org.factoriaf5.user.dtos.UserRegisterRequest;
import org.factoriaf5.user.dtos.UserResponse;

public interface UserService {
  // List<UserRegisterRequest> getAllUsers();

  // UserRegisterRequest getUserByDni(String dni);

  // UserRegisterRequest getUserByEmail(String email);

  // List<UserRegisterRequest> getUsersByRole(String roleName);

  // UserRegisterRequest register(RegisterDTO dto, boolean isAdmin);

  // UserRegisterRequest updateUser(String dni, UpdateUserDTO dto);

  User save(User user);

  void deleteUser(String dni);
}
