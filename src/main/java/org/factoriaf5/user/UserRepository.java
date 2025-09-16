package org.factoriaf5.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByDni(String dni);
    Optional<User> findByUserProfileEmail(String email);
}
