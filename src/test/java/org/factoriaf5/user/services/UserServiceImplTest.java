package org.factoriaf5.user.services;

import org.factoriaf5.user.User;
import org.factoriaf5.user.dtos.UserResponse;
import org.factoriaf5.user.mapper.UserMapper;
import org.factoriaf5.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

  private UserRepository userRepository;
  private UserMapper userMapper;
  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    userMapper = mock(UserMapper.class);
    userService = new UserServiceImpl(userRepository, userMapper);
  }

  @Test
  void saveShouldCallRepositoryAndReturnUser() {
    User user = User.builder().dni("123").build();
    when(userRepository.save(user)).thenReturn(user);

    User saved = userService.save(user);

    assertThat(saved).isEqualTo(user);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void getAllUsersShouldReturnMappedList() {
    User user = User.builder().dni("123").build();
    UserResponse dto = new UserResponse("123", "Carlos", "Pérez", "carlos@example.com", "600111111", "ADMIN");

    when(userRepository.findAll()).thenReturn(List.of(user));
    when(userMapper.toDTO(user)).thenReturn(dto);

    List<UserResponse> result = userService.getAllUsers();

    assertThat(result).containsExactly(dto);
    verify(userRepository).findAll();
    verify(userMapper).toDTO(user);
  }

  @Test
  void deleteUserShouldDeleteWhenExists() {
    String dni = "123";
    when(userRepository.existsById(dni)).thenReturn(true);

    userService.deleteUser(dni);

    verify(userRepository).deleteById(dni);
  }

  @Test
  void deleteUserShouldThrowExceptionWhenNotExists() {
    String dni = "123";
    when(userRepository.existsById(dni)).thenReturn(false);

    assertThatThrownBy(() -> userService.deleteUser(dni))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Dni no encontrado");
  }

  @Test
  void getUserByDniShouldReturnMappedUser() {
    String dni = "123";
    User user = User.builder().dni(dni).build();
    UserResponse dto = new UserResponse("123", "Carlos", "Pérez", "carlos@example.com", "600111111", "ADMIN");

    when(userRepository.findById(dni)).thenReturn(Optional.of(user));
    when(userMapper.toDTO(user)).thenReturn(dto);

    UserResponse result = userService.getUserByDni(dni);

    assertThat(result).isEqualTo(dto);
  }

  @Test
  void getUserByDniShouldThrowExceptionIfNotFound() {
    String dni = "123";
    when(userRepository.findById(dni)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUserByDni(dni))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Usuario no encontrado");
  }

  @Test
  void getUserByEmailShouldReturnMappedUser() {
    String email = "test@example.com";
    User user = User.builder().dni("123").build();
    UserResponse dto = new UserResponse("123", "Carlos", "Pérez", "carlos@example.com", "600111111", "ADMIN");

    when(userRepository.findByProfileEmail(email)).thenReturn(Optional.of(user));
    when(userMapper.toDTO(user)).thenReturn(dto);

    UserResponse result = userService.getUserByEmail(email);

    assertThat(result).isEqualTo(dto);
  }

  @Test
  void getUserByEmailShouldThrowExceptionIfNotFound() {
    String email = "test@example.com";
    when(userRepository.findByProfileEmail(email)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUserByEmail(email))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Usuario no encontrado por email");
  }

  @Test
  void getUsersByRoleShouldReturnMappedUsers() {
    String roleName = "ADMIN";
    User user = User.builder().dni("123").build();
    UserResponse dto = new UserResponse("123", "Carlos", "Pérez", "carlos@example.com", "600111111", "ADMIN");

    when(userRepository.findByRoles_Name(roleName)).thenReturn(List.of(user));
    when(userMapper.toDTO(user)).thenReturn(dto);

    List<UserResponse> result = userService.getUsersByRole(roleName);

    assertThat(result).containsExactly(dto);
  }
}
