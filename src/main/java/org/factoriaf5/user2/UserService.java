package org.factoriaf5.user;

import org.factoriaf5.user.dto.*;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserByDni(String dni);
    UserDTO getUserByEmail(String email);
    List<UserDTO> getUsersByRole(String roleName);
    UserDTO register(RegisterDTO dto, boolean isAdmin);
    UserDTO updateUser(String dni, UpdateUserDTO dto);
    void deleteUser(String dni);
}
