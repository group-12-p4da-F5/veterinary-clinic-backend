package org.factoriaf5.user;

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
    public List<User> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{dni}")
    public ResponseEntity<User> getByDni(@PathVariable String dni) {
        return ResponseEntity.ok(service.getUserByDni(dni));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }

    @GetMapping("/role/{roleName}")
    public List<User> getByRole(@PathVariable String roleName) {
        return service.getUsersByRole(roleName);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(service.createUser(user));
    }

    @PutMapping("/{dni}")
    public ResponseEntity<User> update(@PathVariable String dni, @RequestBody User user) {
        return ResponseEntity.ok(service.updateUser(dni, user));
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        service.deleteUser(dni);
        return ResponseEntity.noContent().build();
    }
}
