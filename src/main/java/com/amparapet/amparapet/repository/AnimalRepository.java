package com.amparapet.amparapet.repository;

import com.amparapet.amparapet.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnimalRepository extends JpaRepository<Animal, Long> {
}

