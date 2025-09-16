package org.factoriaf5.user;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserByDni(String dni);
    User getUserByEmail(String email);
    User createUser(User user);
    User updateUser(String dni, User user);
    void deleteUser(String dni);
}
