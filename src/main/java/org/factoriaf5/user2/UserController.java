package org.factoriaf5.user2;

import org.factoriaf5.user2.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{dni}")
    public ResponseEntity<UserDTO> getByDni(@PathVariable String dni) {
        return ResponseEntity.ok(service.getUserByDni(dni));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    @GetMapping("/role/{roleName}")
    public List<UserDTO> getByRole(@PathVariable String roleName) {
        return service.getUsersByRole(roleName);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(service.register(dto, false));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<UserDTO> registerByAdmin(@RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(service.register(dto, true));
    }

    @PutMapping("/{dni}")
    public ResponseEntity<UserDTO> update(@PathVariable String dni, @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(service.updateUser(dni, dto));
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        service.deleteUser(dni);
        return ResponseEntity.noContent().build();
    }
}
