package org.factoriaf5.user2;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByProfileEmail(String email);

    List<User> findByRole_Name(String roleName); // cambiado para devolver lista
}
