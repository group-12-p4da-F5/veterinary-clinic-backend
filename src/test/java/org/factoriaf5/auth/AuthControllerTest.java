package org.factoriaf5.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

  @Autowired
  WebApplicationContext context;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  @DisplayName("Users can login")
  @WithMockUser // @WithMockUser(username="minnie") podemos personalizar el username y más...
  void testUsersCanLogin() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(get("/api/v1/login"))
        .andExpect(status().isAccepted())
        .andReturn()
        .getResponse();

    assertThat(response.getContentAsString(), containsString("Logged"));
    assertThat(response.getContentAsString(), containsString("user"));
    assertThat(response.getContentAsString(), containsString("ROLE_USER"));
  }

  @Test
  @DisplayName("Unauthenticated users cannot login")
  void testUnauthenticatedUsersCannotLogin() throws Exception {
    mockMvc.perform(get("/api/v1/login"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("If user are ADMIN, should return 200")
  @WithMockUser(roles = "ADMIN")
  void testIfUserAreAdminShouldReturn202() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(get("/api/v1/login"))
        .andExpect(status().isAccepted())
        .andReturn()
        .getResponse();

    assertThat(response.getContentAsString(), containsString("ROLE_ADMIN"));
  }

  @Test
  @DisplayName("If user are USER, should return 200")
  @WithMockUser(roles = "USER")
  void testIfUserAreUserShouldReturn202() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(get("/api/v1/login"))
        .andExpect(status().isAccepted())
        .andReturn()
        .getResponse();

    assertThat(response.getContentAsString(), containsString("ROLE_USER"));
  }

  @Test
  @DisplayName("If user are not ADMIN or USER, should return 403")
  @WithMockUser(roles = "GUESS")
  void testIfUserAreGuessShouldReturn403() throws Exception {
    mockMvc.perform(get("/api/v1/login"))
        .andExpect(status().isForbidden());
  }

  @Test
  void testUserCanLogin() throws Exception {
    MockHttpServletResponse response = mockMvc.perform(
        get("/api/v1/login").with(user("POPP").password("pass").roles("ADMIN")))
        .andExpect(authenticated())
        .andReturn()
        .getResponse();

    assertThat(response.getStatus(), is(HttpStatus.ACCEPTED.value()));
    assertThat(response.getContentAsString(), containsString("Logged"));
    assertThat(response.getContentAsString(), containsString("POPP"));
  }

  @Test
  void testUserCannotAccessToPrivatePath() throws Exception {
    mockMvc.perform(get("/api/v1/private").with(user("POPP").password("pass").roles("USER")))
        .andExpect(authenticated())
        .andExpect(status().isForbidden())
        .andReturn()
        .getResponse();
  }

}
