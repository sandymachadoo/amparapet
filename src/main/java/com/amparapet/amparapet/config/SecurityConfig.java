package com.amparapet.amparapet.config;

import com.amparapet.amparapet.model.Usuario;
import com.amparapet.amparapet.repository.UsuarioRepository;
import com.amparapet.amparapet.security.FiltroJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth


                        .requestMatchers(
                                "/auth/login",
                                "/usuarios/cadastrar",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()


                        .requestMatchers(HttpMethod.GET, "/animais", "/animais/**").permitAll()


                        .requestMatchers(HttpMethod.POST, "/adocoes").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/adocoes").hasAuthority("ROLE_ADMIN")


                        .requestMatchers(HttpMethod.POST, "/animais/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/animais/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/animais/**").hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(filtroJWT, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Usuário não encontrado: " + email)
                    );

            return org.springframework.security.core.userdetails.User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenha())

                    .authorities(usuario.getRole())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://localhost:*");
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        );
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
