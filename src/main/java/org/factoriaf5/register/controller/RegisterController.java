package org.factoriaf5.register.controller;

import org.factoriaf5.register.dtos.RegisterResponse;
import org.factoriaf5.register.service.RegisterService;
import org.factoriaf5.user.dtos.UserRegisterRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("${api-endpoint}/register")
public class RegisterController {

  private final RegisterService service;

  public RegisterController(RegisterService service) {
    this.service = service;
  }

  @PostMapping("")
  public ResponseEntity<RegisterResponse> registerUser(@RequestBody UserRegisterRequest dto) {
    service.registerUser(dto);

    return ResponseEntity.status(201).body(RegisterResponse.builder().message("User stored succesfully").build());
  }

}
