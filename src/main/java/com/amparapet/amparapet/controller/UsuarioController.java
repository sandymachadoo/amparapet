package com.amparapet.amparapet.controller;


import com.amparapet.amparapet.model.Usuario;
import com.amparapet.amparapet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getRole() == null) {
            usuario.setRole("ROLE_USER"); // Define usuário comum por padrão
        }
        Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.status(201).body(usuarioSalvo);
    }


    @GetMapping("/email")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
