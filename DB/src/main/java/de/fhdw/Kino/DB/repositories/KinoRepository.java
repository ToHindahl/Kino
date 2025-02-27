package de.fhdw.Kino.DB.repositories;

import de.fhdw.Kino.DB.domain.Kino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KinoRepository extends JpaRepository<Kino, Long> { }
