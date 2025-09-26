package org.factoriaf5.user2;

import java.util.List;

import org.factoriaf5.user2.dto.*;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserByDni(String dni);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getUsersByRole(String roleName);

    UserDTO register(RegisterDTO dto, boolean isAdmin);

    UserDTO updateUser(String dni, UpdateUserDTO dto);

    void deleteUser(String dni);
}
