package com.amparapet.amparapet.config;

// IMPORTS NECESSÁRIOS PARA O SPRING SECURITY E JWT
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

// 🚨 IMPORTS NECESSÁRIOS PARA O CORS
import java.util.Arrays;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


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
                // 1. Configura CORS e Desabilita CSRF (essencial para APIs)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth

                        // Swagger e OpenAPI
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Rotas públicas (Login e Cadastro)
                        .requestMatchers("/auth/login", "/usuarios/cadastrar").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // 🚨 MUDANÇA AQUI: Garante que LISTA e BUSCA por ID (/animais/**)
                        // são acessíveis por QUALQUER USUÁRIO AUTENTICADO (ADMIN ou USER)
                        .requestMatchers(HttpMethod.GET, "/animais", "/animais/**").authenticated()


                        // Rotas de adoção
                        .requestMatchers(HttpMethod.POST, "/adocoes").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/adocoes").hasRole("ADMIN")

                        // Rotas de animais (admin)
                        .requestMatchers(HttpMethod.POST, "/animais/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/animais/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/animais/**").hasRole("ADMIN")

                        // Qualquer outra rota precisa autenticação
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filtroJWT, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ... (Os Beans CorsConfigurationSource, UserDetailsService, PasswordEncoder e AuthenticationManager permanecem os mesmos) ...

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Adicionei PATCH
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenha())
                    .roles(usuario.getRole().replace("ROLE_", ""))
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
}