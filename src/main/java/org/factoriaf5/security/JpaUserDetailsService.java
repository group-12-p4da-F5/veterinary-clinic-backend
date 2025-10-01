package org.factoriaf5.security;

import org.factoriaf5.user.User;
import org.factoriaf5.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public JpaUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
    User user = userRepository.findById(dni)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario con DNI " + dni + " no encontrado"));

    return new SecurityUser(user);
  }

}
