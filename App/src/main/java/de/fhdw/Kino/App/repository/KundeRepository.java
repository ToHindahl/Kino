package de.fhdw.Kino.App.repository;

import de.fhdw.Kino.App.domain.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KundeRepository extends JpaRepository<Kunde, Long> { }
