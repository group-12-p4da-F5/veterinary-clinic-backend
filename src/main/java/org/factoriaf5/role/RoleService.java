package org.factoriaf5.role;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class RoleService {

  private final RoleRepository repository;

  public RoleService(RoleRepository repository) {
    this.repository = repository;
  }

  public Role getById(Long id) {
    return repository.findById(id).orElseThrow(); // TODO: se devería devolver una exception concreta que no dará tiempo
                                                  // a hacer :()
  }

  public Set<Role> assignDefaultRole() {
    Role defaultRole = this.getById(2L);

    Set<Role> roles = new HashSet<>();
    roles.add(defaultRole);

    return roles;
  }
}
