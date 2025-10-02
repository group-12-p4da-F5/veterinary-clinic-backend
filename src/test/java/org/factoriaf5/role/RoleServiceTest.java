package org.factoriaf5.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleServiceTest {

  private RoleRepository roleRepository;
  private RoleService roleService;

  @BeforeEach
  void setUp() {
    roleRepository = mock(RoleRepository.class);
    roleService = new RoleService(roleRepository);
  }

  @Test
  void getByIdShouldReturnRoleIfExists() {
    Role role = new Role();
    role.setId(1L);
    role.setName("ADMIN");

    when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

    Role result = roleService.getById(1L);

    assertThat(result).isEqualTo(role);
    verify(roleRepository).findById(1L);
  }

  @Test
  void getByIdShouldThrowIfNotExists() {
    when(roleRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> roleService.getById(99L))
        .isInstanceOf(Exception.class);
  }

  @Test
  void assignDefaultRoleShouldReturnSetWithDefaultRole() {
    Role defaultRole = new Role();
    defaultRole.setId(2L);
    defaultRole.setName("USER");

    when(roleRepository.findById(2L)).thenReturn(Optional.of(defaultRole));

    Set<Role> result = roleService.assignDefaultRole();

    assertThat(result).containsExactly(defaultRole);
    verify(roleRepository).findById(2L);
  }
}
