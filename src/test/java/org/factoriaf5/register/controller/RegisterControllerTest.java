package org.factoriaf5.register.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.factoriaf5.register.dtos.RegisterResponse;
import org.factoriaf5.user.dtos.UserRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "11111111M", roles = { "ADMIN" })
@ActiveProfiles("test")
class RegisterControllerTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper mapper;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  @DisplayName("A new user can register successfully")
  void testRegisterUser() throws Exception {
    UserRegisterRequest request = new UserRegisterRequest(
        "12345678A",
        "Mickey",
        "Mouse",
        "mickey@disney.com",
        "123456789",
        "securePass");

    mockMvc.perform(post("/api/v1/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("User stored succesfully"));
  }
}
