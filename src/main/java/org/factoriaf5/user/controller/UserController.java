package org.factoriaf5.user.controller;

import java.util.List;

import org.factoriaf5.user.dtos.UserResponse;
import org.factoriaf5.user.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("${api-endpoint}/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping("")
  public List<UserResponse> getAllUsers() {
    return service.getAllUsers();
  }

  @GetMapping("/{dni}")
  public ResponseEntity<UserResponse> getByDni(@PathVariable String dni) {
    return ResponseEntity.ok(service.getUserByDni(dni));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<UserResponse> getByEmail(@PathVariable String email) {
    return ResponseEntity.ok(service.getUserByEmail(email));
  }

  @GetMapping("/role/{roleName}")
  public List<UserResponse> getByRole(@PathVariable String roleName) {
    return service.getUsersByRole(roleName);
  }

  @DeleteMapping("/{dni}")
  public ResponseEntity<Void> delete(@PathVariable String dni) {
    service.deleteUser(dni);
    return ResponseEntity.noContent().build();
  }

}
