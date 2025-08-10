package com.amparapet.amparapet.controller;

import com.amparapet.amparapet.dto.AdocaoDTO;
import com.amparapet.amparapet.model.Adocao;
import com.amparapet.amparapet.service.AdocaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> registrarAdocao(@RequestBody AdocaoDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Adocao adocao = adocaoService.salvarAdocao(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(adocao);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Adocao>> listarTodasAdocoes() {
        return ResponseEntity.ok(adocaoService.listarTodas());
    }
}