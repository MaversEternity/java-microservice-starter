package com.me;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.me.config.security.AuthenticatedUser;

/**
 * Global security context mock
 */
public interface SecurityMock {

  Long USER_ID = 1L;


  @BeforeAll
  static void init() {
    initSecurityContext(Set.of());
  }

  private static void initSecurityContext(Set<GrantedAuthority> authorities) {
    AuthenticatedUser user = new AuthenticatedUser(USER_ID, authorities);
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
  }

}
