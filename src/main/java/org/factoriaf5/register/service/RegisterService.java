package org.factoriaf5.register.service;

import org.factoriaf5.encrypt.decrypt.IDecryptFacade;
import org.factoriaf5.encrypt.encrypt.IEncryptFacade;
import org.factoriaf5.role.RoleService;
import org.factoriaf5.user.User;
import org.factoriaf5.user.dtos.UserRegisterRequest;
import org.factoriaf5.user.dtos.UserResponse;
import org.factoriaf5.user.mapper.UserMapper;
import org.factoriaf5.user.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

  private final UserService userService;
  private final RoleService roleService;
  private final UserMapper userMapper;

  private final IDecryptFacade decryptFacade;
  private final IEncryptFacade encryptFacade;

  public RegisterService(UserService userService, RoleService roleService, IDecryptFacade decryptFacade,
      IEncryptFacade encryptFacade, UserMapper userMapper) {
    this.userService = userService;
    this.roleService = roleService;
    this.decryptFacade = decryptFacade;
    this.encryptFacade = encryptFacade;
    this.userMapper = userMapper;
  }

  public UserResponse registerUser(UserRegisterRequest dto) {

    String passwordDecoded = decryptFacade.decode("base64", dto.password());
    String hashedPassword = encryptFacade.encode("bcrypt", passwordDecoded);

    User user = userMapper.toEntity(dto, hashedPassword, roleService.assignDefaultRole());
    User saved = userService.save(user);

    return userMapper.toDTO(saved);
  }
}
