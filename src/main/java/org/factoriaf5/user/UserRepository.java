package org.factoriaf5.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByDni(String dni);

    @Query("SELECT u FROM User u WHERE u.profile.email = :email")
    Optional<User> findByProfileEmail(@Param("email") String email);
}
