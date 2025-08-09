package com.amparapet.amparapet.repository;

import com.amparapet.amparapet.model.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdocaoRepository extends JpaRepository<Adocao, Long> {
    List<Adocao> findByAnimalId(Long animalId);
}
