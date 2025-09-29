package org.factoriaf5.user.services;

import java.util.List;

import org.factoriaf5.user.User;
import org.factoriaf5.user.dtos.UserResponse;

public interface UserService {

  User save(User user);

  List<UserResponse> getAllUsers();

  void deleteUser(String dni);

  UserResponse getUserByDni(String dni);

  UserResponse getUserByEmail(String email);

  List<UserResponse> getUsersByRole(String roleName);

}
