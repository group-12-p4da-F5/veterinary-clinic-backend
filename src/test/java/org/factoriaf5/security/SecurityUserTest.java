package org.factoriaf5.security;

import org.factoriaf5.role.Role;
import org.factoriaf5.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityUserTest {

  private User user;

  @BeforeEach
  void setUp() {
    Role roleUser = new Role();
    roleUser.setId(1L);
    roleUser.setName("USER");

    Role roleAdmin = new Role();
    roleAdmin.setId(2L);
    roleAdmin.setName("ADMIN");

    user = new User();
    user.setDni("12345678A");
    user.setPasswordHash("hashedPassword");
    user.setRoles(Set.of(roleUser, roleAdmin));
  }

  @Test
  void getUsernameShouldReturnDni() {
    SecurityUser securityUser = new SecurityUser(user);

    assertThat(securityUser.getUsername()).isEqualTo("12345678A");
  }

  @Test
  void getPasswordShouldReturnPasswordHash() {
    SecurityUser securityUser = new SecurityUser(user);

    assertThat(securityUser.getPassword()).isEqualTo("hashedPassword");
  }

  @Test
  void getAuthoritiesShouldReturnMappedRoles() {
    SecurityUser securityUser = new SecurityUser(user);

    var authorities = securityUser.getAuthorities();

    assertThat(authorities)
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
  }

  @Test
  void accountFlagsShouldBeAlwaysTrue() {
    SecurityUser securityUser = new SecurityUser(user);

    assertThat(securityUser.isAccountNonExpired()).isTrue();
    assertThat(securityUser.isAccountNonLocked()).isTrue();
    assertThat(securityUser.isCredentialsNonExpired()).isTrue();
    assertThat(securityUser.isEnabled()).isTrue();
  }
}
