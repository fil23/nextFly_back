package com.nextfly.demo.db.chiavi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextfly.demo.db.chiavi.entities.ChiaviEntity;

@Repository
public interface ChiaviRepo extends JpaRepository<ChiaviEntity, Long> {
    ChiaviEntity findChiaviEntityByDescrizione(String descrizione);
}
