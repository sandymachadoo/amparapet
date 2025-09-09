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
    public ResponseEntity<AdocaoDTO> registrarAdocao(@RequestBody AdocaoDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Adocao adocao = adocaoService.salvarAdocao(dto, email);
        AdocaoDTO adocaoDTO = adocaoService.toDTO(adocao);
        return ResponseEntity.status(HttpStatus.CREATED).body(adocaoDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdocaoDTO>> listarTodasAdocoes() {
        List<AdocaoDTO> adocoes = adocaoService.listarTodasDTO();
        return ResponseEntity.ok(adocoes);
    }

    @GetMapping("/animal/{animalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdocaoDTO>> listarPorAnimal(@PathVariable Long animalId) {
        List<AdocaoDTO> adocoes = adocaoService.listarPorAnimalIdDTO(animalId);
        return ResponseEntity.ok(adocoes);
    }
}
