package org.factoriaf5.role;

import org.factoriaf5.user.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

  @Test
  void shouldCreateRoleWithSetters() {
    Role role = new Role();
    role.setId(1L);
    role.setName("ADMIN");

    assertThat(role.getId()).isEqualTo(1L);
    assertThat(role.getName()).isEqualTo("ADMIN");
  }

  @Test
  void shouldAllowSettingAndGettingUsers() {
    User user = User.builder().dni("12345678A").build();

    Role role = new Role();
    role.setName("VET");
    role.setUsers(Set.of(user));

    assertThat(role.getUsers()).hasSize(1);
    assertThat(role.getUsers().iterator().next().getDni()).isEqualTo("12345678A");
  }

  @Test
  void equalsShouldReturnTrueForSameIdAndName() {
    Role role1 = new Role();
    role1.setId(1L);
    role1.setName("ADMIN");

    Role role2 = new Role();
    role2.setId(1L);
    role2.setName("ADMIN");

    assertThat(role1).isEqualTo(role2);
    assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
  }

  @Test
  void equalsShouldReturnFalseForDifferentIds() {
    Role role1 = new Role();
    role1.setId(1L);
    role1.setName("ADMIN");

    Role role2 = new Role();
    role2.setId(2L);
    role2.setName("ADMIN");

    assertThat(role1).isNotEqualTo(role2);
  }

  @Test
  void equalsShouldReturnFalseWhenIdIsNull() {
    Role role1 = new Role();
    role1.setId(null);
    role1.setName("ADMIN");

    Role role2 = new Role();
    role2.setId(1L);
    role2.setName("ADMIN");

    assertThat(role1).isNotEqualTo(role2);
  }

}
