package com.amparapet.amparapet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FiltroJWT extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public FiltroJWT(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("🔎 FiltroJWT - Request URI: " + path);

        if (path.equals("/auth/login") || path.equals("/usuarios/cadastrar")) {
            System.out.println("➡️ Acesso liberado (rota pública)");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);
            String email = jwtUtil.validarToken(jwt);
            String role = jwtUtil.obterRole(jwt);

            System.out.println("✔️ Token válido");
            System.out.println("   Email: " + email);
            System.out.println("   Role (raw): " + role);

            if (email != null && role != null) {


                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }

                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("✅ SecurityContext atualizado com authority: " + role);
            }

        } else {
            System.out.println("❌ Authorization header ausente ou inválido");
        }

        filterChain.doFilter(request, response);
    }
}
