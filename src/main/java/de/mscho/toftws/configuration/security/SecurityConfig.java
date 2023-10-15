package de.mscho.toftws.configuration.security;

import de.mscho.toftws.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(
                        authorizeHttp -> authorizeHttp
                                .requestMatchers(antMatcher("favicon.svg")).permitAll()
                                .requestMatchers(antMatcher("/css/*")).permitAll()
                                .requestMatchers(antMatcher("/error")).permitAll()
                                .anyRequest().authenticated()
                )
                .apply(new ApiConfigurer(userService)).and()
                .csrf(configurer -> configurer.ignoringRequestMatchers(antMatcher("/calendar/**")))
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }
}
