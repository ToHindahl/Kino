package de.fhdw.Kino.DB.repository;

import de.fhdw.Kino.DB.model.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KundeRepository extends JpaRepository<Kunde, Long> { }
