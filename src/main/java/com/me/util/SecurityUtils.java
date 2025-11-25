package com.me.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.me.config.security.AuthenticatedUser;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {

    /**
     * @return Get user context
     */
    public static AuthenticatedUser getAuthContext() {
        Optional<AuthenticatedUser> user = findAuthContext();

        if (user.isEmpty()) {
            throw new IllegalStateException("Expecting user to be authenticated");
        }

        return user.get();
    }

    /**
     * @return Get user context
     */
    public static Optional<AuthenticatedUser> findAuthContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getPrincipal)
            .filter(AuthenticatedUser.class::isInstance)
            .map(AuthenticatedUser.class::cast);
    }

}
