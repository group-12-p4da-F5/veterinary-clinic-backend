package org.factoriaf5.user;

import org.factoriaf5.user.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserRoleRepository roleRepository;

    public UserServiceImpl(UserRepository repository, UserRoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO getUserByDni(String dni) {
        return repository.findById(dni)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return repository.findByProfileEmail(email)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado por email"));
    }

    @Override
    public List<UserDTO> getUsersByRole(String roleName) {
        return repository.findByRole_Name(roleName).stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO register(RegisterDTO dto, boolean isAdmin) {
        String roleName = isAdmin ? "ADMIN" : "USER";
        UserRole role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleName));

        // ⚠️ Aquí NO encriptamos, se guarda la contraseña tal cual
        User user = UserMapper.toEntity(dto, dto.getPassword(), role);

        return UserMapper.toDTO(repository.save(user));
    }

    @Override
    public UserDTO updateUser(String dni, UpdateUserDTO dto) {
        User existing = repository.findById(dni)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        UserMapper.updateEntity(existing, dto);
        return UserMapper.toDTO(repository.save(existing));
    }

    @Override
    public void deleteUser(String dni) {
        repository.deleteById(dni);
    }
}
