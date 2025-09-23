package org.factoriaf5.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserByDni(String dni) {
        return repository.findById(dni)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByProfileEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado por email"));
    }

    @Override
    public List<User> getUsersByRole(String roleName) {
        return repository.findByRole_Name(roleName)
                .map(List::of)
                .orElse(List.of());
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(String dni, User user) {
        User existing = getUserByDni(dni);
        existing.setPasswordHash(user.getPasswordHash());
        existing.setRole(user.getRole());
        existing.setProfile(user.getProfile()); // actualizar perfil si viene
        return repository.save(existing);
    }

    @Override
    public void deleteUser(String dni) {
        repository.deleteById(dni);
    }
}
