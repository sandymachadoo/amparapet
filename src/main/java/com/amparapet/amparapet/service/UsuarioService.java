package com.amparapet.amparapet.service;

import com.amparapet.amparapet.model.Usuario;
import com.amparapet.amparapet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario salvarUsuario(Usuario usuario) {
        if (usuario.getRole() == null) {
            usuario.setRole("ROLE_USER");
        } else if (!usuario.getRole().startsWith("ROLE_")) {
            usuario.setRole("ROLE_" + usuario.getRole().toUpperCase());
        }
        return usuarioRepository.save(usuario);
    }
}

