package org.factoriaf5.user.repository;

import java.util.List;
import java.util.Optional;

import org.factoriaf5.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByProfileEmail(String email);

  List<User> findByRoles_Name(String roleName);
}