package de.fhdw.Kino.DB.repositories;

import de.fhdw.Kino.DB.domain.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KundeRepository extends JpaRepository<Kunde, Long> { }
