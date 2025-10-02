package org.factoriaf5.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.factoriaf5.patient.PatientRepository;
import org.factoriaf5.role.Role;
import org.factoriaf5.role.RoleRepository;
import org.factoriaf5.user.User;
import org.factoriaf5.user.UserProfile;
import org.factoriaf5.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "11111111M", roles = { "ADMIN" })
@ActiveProfiles("test")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private RoleRepository roleRepository;

  @BeforeEach
  void setUp() {
    patientRepository.deleteAll();
    userRepository.deleteAll();
    roleRepository.deleteAll();

    Role role = new Role();
    role.setName("ADMIN");
    role = roleRepository.save(role);

    User user = User.builder()
        .dni("11111111M")
        .passwordHash("margarita123")
        .roles(Set.of(role))
        .build();

    UserProfile profile = UserProfile.builder()
        .firstName("Test")
        .lastName("User")
        .email("test@example.com")
        .phoneNumber("123456789")
        .user(user)
        .build();

    user.setProfile(profile);

    userRepository.save(user);
  }

  @Test
  @DisplayName("GET /users devuelve todos los usuarios desde H2")
  void testGetAllUsers() throws Exception {
    mockMvc.perform(get("/api/v1/users")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].dni").value("11111111M"));
  }

  @Test
  @DisplayName("GET /users/{dni} devuelve un usuario existente")
  void testGetUserByDni() throws Exception {
    mockMvc.perform(get("/api/v1/users/11111111M"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.dni").value("11111111M"));
  }

  @Test
  @DisplayName("GET /users/role/{role} lista usuarios por rol")
  void testGetUsersByRole() throws Exception {
    mockMvc.perform(get("/api/v1/users/role/ADMIN"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1));
  }

  @Test
  @DisplayName("DELETE /users/{dni} borra un usuario")
  void testDeleteUser() throws Exception {
    mockMvc.perform(delete("/api/v1/users/11111111M"))
        .andExpect(status().isNoContent());

    // mockMvc.perform(get("/api/v1/users/11111111M"))
    // .andExpect(status().is4xxClientError());
  }

}
