package com.amparapet.amparapet.config;

import com.amparapet.amparapet.model.Usuario;
import com.amparapet.amparapet.repository.UsuarioRepository;
import com.amparapet.amparapet.security.FiltroJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final FiltroJWT filtroJWT;
    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(FiltroJWT filtroJWT, @Lazy UsuarioRepository usuarioRepository) {
        this.filtroJWT = filtroJWT;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/adocoes").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/adocoes").hasRole("ADMIN")       
                        .requestMatchers("/auth/login", "/usuarios/cadastrar").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/animais/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/animais/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/animais/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/animais/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filtroJWT, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenha())
                    .roles(usuario.getRole().replace("ROLE_", ""))  // remove prefixo se tiver
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // aceita senha texto puro
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
