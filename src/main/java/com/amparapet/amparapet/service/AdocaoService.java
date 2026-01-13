package com.amparapet.amparapet.service;

import com.amparapet.amparapet.dto.AdocaoDTO;
import com.amparapet.amparapet.dto.AnimalDTO;
import com.amparapet.amparapet.dto.UsuarioDTO;
import com.amparapet.amparapet.model.Adocao;
import com.amparapet.amparapet.model.Animal;
import com.amparapet.amparapet.model.Usuario;
import com.amparapet.amparapet.repository.AdocaoRepository;
import com.amparapet.amparapet.repository.AnimalRepository;
import com.amparapet.amparapet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        if (dto.getAnimal() == null || dto.getAnimal().getId() == null) {
            throw new IllegalArgumentException("Animal não informado para adoção");
        }


        Animal animal = animalRepository.findById(dto.getAnimal().getId())
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

    public List<AdocaoDTO> listarTodasDTO() {
        return adocaoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AdocaoDTO> listarPorAnimalIdDTO(Long animalId) {
        return adocaoRepository.findByAnimalId(animalId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AdocaoDTO toDTO(Adocao adocao) {

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                adocao.getUsuario().getNome(),
                adocao.getUsuario().getEmail()
        );

        AnimalDTO animalDTO = new AnimalDTO(
                adocao.getAnimal().getId(),
                adocao.getAnimal().getNome(),
                adocao.getAnimal().getIdade(),
                adocao.getAnimal().getEspecie(),
                adocao.getAnimal().getRaca(),
                adocao.getAnimal().getDescricao()
        );

        AdocaoDTO dto = new AdocaoDTO();

        dto.setId(adocao.getId());

        dto.setTelefone(adocao.getTelefone());
        dto.setTipoResidencia(adocao.getTipoResidencia());
        dto.setEstado(adocao.getEstado());
        dto.setCidade(adocao.getCidade());
        dto.setUsuario(usuarioDTO);
        dto.setAnimal(animalDTO);

        return dto;
    }
}
