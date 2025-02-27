package de.fhdw.Kino.DB.repository;

import de.fhdw.Kino.DB.model.Kino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KinoRepository extends JpaRepository<Kino, Long> { }
