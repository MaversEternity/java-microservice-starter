package com.me.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.me.config.logging.LoggingFilterImpl;
import com.me.config.logging.LoggingFormat;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${logging.format}")
    private LoggingFormat loggingFormat;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

        http.authorizeHttpRequests(ahr -> ahr.anyRequest().authenticated())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
            .securityContext(sc -> sc.securityContextRepository(securityContextRepository))
            .exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint))
            .addFilterBefore(new LoggingFilterImpl(loggingFormat), BasicAuthenticationFilter.class)
            .addFilterBefore(new HeaderContextFilter(securityContextRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService myUserDetailsService() {
        return _ -> { throw new UsernameNotFoundException("Not used"); };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring()
            .requestMatchers("/public/**", "/swagger-ui/**", "/contract/async/**", "/swagger-ui.html", "/swagger-resources/**",
                "/v2/api-docs", "/v3/api-docs/**");
    }

}
