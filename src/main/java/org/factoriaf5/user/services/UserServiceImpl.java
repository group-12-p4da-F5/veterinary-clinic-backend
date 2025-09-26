package org.factoriaf5.user.services;

import org.factoriaf5.user.User;
import org.factoriaf5.user.dtos.UserRegisterRequest;
import org.factoriaf5.user.dtos.UserResponse;
import org.factoriaf5.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.factoriaf5.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public User save(User user) {

    return userRepository.save(user);
  }

  @Override
  public void deleteUser(String dni) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }

}
