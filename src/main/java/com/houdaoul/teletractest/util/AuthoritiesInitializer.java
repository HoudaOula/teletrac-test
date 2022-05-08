package com.houdaoul.teletractest.util;

import com.houdaoul.teletractest.domain.Authority;
import com.houdaoul.teletractest.domain.User;
import com.houdaoul.teletractest.domain.dto.UserDto;
import com.houdaoul.teletractest.repository.AuthorityRepository;
import com.houdaoul.teletractest.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthoritiesInitializer implements CommandLineRunner {
  
  public final UserService userService;
  private final AuthorityRepository authorityRepository;
  
  public AuthoritiesInitializer(UserService userService,
                                AuthorityRepository authorityRepository) {
    this.userService = userService;
    this.authorityRepository = authorityRepository;
  }
  
  
  @Override
  @Transactional
  public void run(String... args) throws Exception {
    Authority userAuthority = Authority.builder().name("USER").build();
    Authority adminAuthority = Authority.builder().name("ADMIN").build();

    authorityRepository.findById(userAuthority.getName())
        .orElseGet(() -> authorityRepository.save(userAuthority));

    authorityRepository.findById(adminAuthority.getName())
        .orElseGet(() -> authorityRepository.save(adminAuthority));

    UserDto userDto = UserDto.builder()
            .username("admin")
            .password("admin")
            .build();
    
    userService.getUserByUsername(userDto.getUsername())
        .orElseGet(() -> {
          try {
            return userService.saveAdmin(userDto).orElseThrow(Exception::new);
          } catch (Exception e) {
            e.printStackTrace();
            return null;
          }
        });
  }
}
