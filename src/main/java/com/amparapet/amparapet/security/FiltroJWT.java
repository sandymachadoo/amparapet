package com.amparapet.amparapet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        System.out.println("FiltroJWT - Request URI: " + path);
        System.out.println("FiltroJWT - Context Path: " + contextPath);

        if (path.startsWith(contextPath + "/auth/login") || path.startsWith(contextPath + "/usuarios/cadastrar")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7); // Remove "Bearer " do início
            String email = jwtUtil.validarToken(jwt);
            String role = jwtUtil.obterRole(jwt);

            System.out.println("FiltroJWT - Token válido para email: " + email + ", role: " + role);

            if (email != null && role != null) {
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority(role)
                );

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        email, null, authorities
                );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } else {
            System.out.println("FiltroJWT - Token ausente ou mal formatado");
        }

        filterChain.doFilter(request, response);
    }
}
