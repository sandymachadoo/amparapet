package com.amparapet.amparapet.controller;

import com.amparapet.amparapet.dto.LoginResponseDTO;
import com.amparapet.amparapet.dto.UsuarioLoginDTO;
import com.amparapet.amparapet.model.Usuario;
import com.amparapet.amparapet.repository.UsuarioRepository;
import com.amparapet.amparapet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDTO loginDTO) {
        System.out.println("Tentativa de login com email: " + loginDTO.getEmail());

        try {
            System.out.println("-> Tentando autenticar...");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
            );
            System.out.println("-> Autenticação bem-sucedida!");

            Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginDTO.getEmail());
            if (optionalUsuario.isEmpty()) {
                System.out.println("-> Usuário não encontrado no banco");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
            }

            Usuario usuario = optionalUsuario.get();
            System.out.println("-> Usuário encontrado: " + usuario.getEmail() + ", role: " + usuario.getRole());



            String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getRole());
            System.out.println("-> Token gerado com sucesso: " + token);

            LoginResponseDTO responseDTO = new LoginResponseDTO(token, usuario.getRole());

            return ResponseEntity.ok(responseDTO);

        } catch (AuthenticationException e) {
            System.out.println("-> Falha ao autenticar: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        } catch (Exception ex) {
            System.out.println("-> Erro inesperado: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }
}