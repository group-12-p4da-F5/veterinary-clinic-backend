package org.factoriaf5.home;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HomeController {

  @GetMapping()
  public ResponseEntity<HomeDTOResponse> index() {
    return ResponseEntity.ok().body(new HomeDTOResponse(HttpStatus.OK.value(), "Welcome to Veterinary Clinic API"));
  }

  @GetMapping("/public")
  public ResponseEntity<HomeDTOResponse> publicPath() {
    return ResponseEntity.ok().body(new HomeDTOResponse(HttpStatus.OK.value(), "Public path"));
  }

  @GetMapping("/private")
  public ResponseEntity<HomeDTOResponse> privatePath() {
    return ResponseEntity.ok().body(new HomeDTOResponse(HttpStatus.OK.value(), "Private path"));
  }
}
