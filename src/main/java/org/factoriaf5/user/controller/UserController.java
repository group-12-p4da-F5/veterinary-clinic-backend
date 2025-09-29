package org.factoriaf5.user.controller;

import java.util.List;

import org.factoriaf5.user.dtos.UserResponse;
import org.factoriaf5.user.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

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

}
