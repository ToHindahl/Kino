package de.fhdw.Kino.App.repository;

import de.fhdw.Kino.App.domain.Kino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KinoRepository extends JpaRepository<Kino, Long> { }
