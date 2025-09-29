package org.factoriaf5.user.services;

import java.util.List;

import org.factoriaf5.user.User;
import org.factoriaf5.user.dtos.UserResponse;
import org.factoriaf5.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.factoriaf5.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public User save(User user) {

    return userRepository.save(user);
  }

  @Override
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream()
        .map(userMapper::toDTO)
        .toList();
  }

  @Override
  public void deleteUser(String dni) {
    if (!userRepository.existsById(dni)) {
      throw new IllegalArgumentException("Dni no encontrado");
    }
    userRepository.deleteById(dni);
  }

  @Override
  public UserResponse getUserByDni(String dni) {
    return userRepository.findById(dni)
        .map(userMapper::toDTO)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
  }

  @Override
  public UserResponse getUserByEmail(String email) {
    return userRepository.findByProfileEmail(email)
        .map(userMapper::toDTO)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado por email"));
  }

  @Override
  public List<UserResponse> getUsersByRole(String roleName) {
    return userRepository.findByRoles_Name(roleName).stream()
        .map(userMapper::toDTO)
        .toList();
  }

}
