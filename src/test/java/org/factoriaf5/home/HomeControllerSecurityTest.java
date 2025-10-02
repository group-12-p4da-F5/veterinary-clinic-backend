package org.factoriaf5.home;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
public class HomeControllerSecurityTest {

  @Autowired
  private WebApplicationContext context;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  @DisplayName("Not authorazed user cannot make a request to root path '/'")
  @WithAnonymousUser
  public void testNotAuthorizedUser_CannotMakeRequestToRoot() throws Exception {
    mockMvc.perform(get(""))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Authenticated user can make a request to root path '/'")
  @WithMockUser
  void testAuthenticatedUser_CanMakeRequestToRoot() throws Exception {
    mockMvc.perform(get(""))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Unauthenticated user can make a request to /public path")
  void testAnonymousUser_CanMakeRequestToPublicPath() throws Exception {
    mockMvc.perform(get("/public"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Authenticated user can make a request to /private path")
  @WithMockUser
  void testAuthenticatedUserCanMakeRequest_ToPrivatePath() throws Exception {
    mockMvc.perform(get("/private"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Unauthenticated user cannot make a request to /private path")
  void testUnauthenticatedUserCannotMakeRequest_ToPrivatePath() throws Exception {
    mockMvc.perform(get("/private"))
        .andExpect(status().isUnauthorized());
  };

}
