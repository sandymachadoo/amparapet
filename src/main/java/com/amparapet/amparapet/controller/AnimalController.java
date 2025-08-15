package com.amparapet.amparapet.controller;

import com.amparapet.amparapet.model.Animal;
import com.amparapet.amparapet.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<Animal> cadastrar(@RequestBody Animal animal) {
        return ResponseEntity.ok(animalRepository.save(animal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> editar(@PathVariable Long id, @RequestBody Animal novo) {
        return animalRepository.findById(id)
                .map(animal -> {
                    animal.setNome(novo.getNome());
                    animal.setRaca(novo.getRaca());
                    animal.setDescricao(novo.getDescricao());
                    animal.setImagemUrl(novo.getImagemUrl());
                    animal.setIdade(novo.getIdade());
                    return ResponseEntity.ok(animalRepository.save(animal));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        animalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Animal> listar() {
        return animalRepository.findAll();
    }
}
