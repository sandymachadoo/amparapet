package com.amparapet.amparapet.service;

import com.amparapet.amparapet.dto.AdocaoDTO;
import com.amparapet.amparapet.model.Adocao;
import com.amparapet.amparapet.model.Animal;
import com.amparapet.amparapet.model.Usuario;
import com.amparapet.amparapet.repository.AdocaoRepository;
import com.amparapet.amparapet.repository.AnimalRepository;
import com.amparapet.amparapet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Adocao salvarAdocao(AdocaoDTO dto, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));

        Adocao adocao = new Adocao();
        adocao.setTelefone(dto.getTelefone());
        adocao.setTipoResidencia(dto.getTipoResidencia());
        adocao.setEstado(dto.getEstado());
        adocao.setCidade(dto.getCidade());
        adocao.setAnimal(animal);
        adocao.setUsuario(usuario);

        return adocaoRepository.save(adocao);
    }

    public List<Adocao> listarTodas() {
        return adocaoRepository.findAll();
    }
}
