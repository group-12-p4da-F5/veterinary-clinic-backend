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
        return repository.findByDni(dni)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
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
        return repository.save(existing);
    }

    @Override
    public void deleteUser(String dni) {
        repository.deleteById(dni);
    }
}
