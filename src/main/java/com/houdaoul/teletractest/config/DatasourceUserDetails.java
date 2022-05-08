package com.houdaoul.teletractest.config;

import com.houdaoul.teletractest.domain.User;
import com.houdaoul.teletractest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatasourceUserDetails implements UserDetailsService {
  
  private final UserRepository userRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final Optional<User> user = userRepository.findById(username);
    return user.orElseThrow(
        () -> new UsernameNotFoundException("Could not find username " + username));
  }
}
