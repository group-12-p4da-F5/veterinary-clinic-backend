package org.factoriaf5.user;

import org.factoriaf5.role.Role;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

  @Test
  void shouldCreateUserWithBuilder() {
    User user = User.builder()
        .dni("12345678A")
        .passwordHash("hashedPass")
        .build();

    assertThat(user.getDni()).isEqualTo("12345678A");
    assertThat(user.getPasswordHash()).isEqualTo("hashedPass");
  }

  @Test
  void shouldAllowSettingAndGettingRoles() {
    Role role = new Role();
    role.setName("ADMIN");

    User user = new User();
    user.setRoles(Set.of(role));

    assertThat(user.getRoles()).hasSize(1);
    assertThat(user.getRoles().iterator().next().getName()).isEqualTo("ADMIN");
  }

  @Test
  void shouldAllowSettingAndGettingProfile() {
    UserProfile profile = new UserProfile();
    User user = new User();
    profile.setUser(user);
    user.setProfile(profile);

    assertThat(user.getProfile()).isNotNull();
    assertThat(user.getProfile().getUser()).isEqualTo(user);
  }

  @Test
  void equalsShouldReturnTrueForSameDni() {
    User user1 = User.builder().dni("12345678A").build();
    User user2 = User.builder().dni("12345678A").build();

    assertThat(user1).isEqualTo(user2);
    assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
  }

  @Test
  void equalsShouldReturnFalseForDifferentDni() {
    User user1 = User.builder().dni("12345678A").build();
    User user2 = User.builder().dni("87654321B").build();

    assertThat(user1).isNotEqualTo(user2);
  }

  @Test
  void equalsShouldReturnFalseWhenDniIsNull() {
    User user1 = User.builder().dni(null).build();
    User user2 = User.builder().dni("12345678A").build();

    assertThat(user1).isNotEqualTo(user2);
  }
}
