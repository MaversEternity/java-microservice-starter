package com.me.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import com.me.common.Constants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.me.common.Constants.X_USER_ID_HEADER;
import static com.me.util.HttpUtils.getRequiredHeaderValue;

/**
 * Auth header enricher
 */
@Slf4j
@RequiredArgsConstructor
public class HeaderContextFilter extends OncePerRequestFilter {

  private final SecurityContextRepository securityContextRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws IOException, ServletException {

      AuthenticatedUser user = constructUserFromRequest(request);

      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

      SecurityContext securityContext = SecurityContextHolder.getContext();
      securityContext.setAuthentication(token);
      securityContextRepository.saveContext(securityContext, request, response);

      filterChain.doFilter(request, response);
  }

  private AuthenticatedUser constructUserFromRequest(HttpServletRequest request) {
    Long userId = Long.parseLong(getRequiredHeaderValue(X_USER_ID_HEADER, request));
    Set<GrantedAuthority> authorities = getPermissionAuthorities(request);

    return new AuthenticatedUser(userId, authorities);
  }

  private Set<GrantedAuthority> getPermissionAuthorities(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(Constants.X_PERMISSIONS_HEADER)).map(it -> it.split(","))
        .map(Arrays::asList)
        .stream()
        .flatMap(List::stream)
        .map(String::trim)
        .map(String::toUpperCase)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

}
