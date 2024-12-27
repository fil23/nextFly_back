package com.nextfly.demo.db.utente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextfly.demo.db.utente.entities.UtenteEntity;

@Repository
public interface UtenteRepo extends JpaRepository<UtenteEntity, Long> {

}
