package de.fhdw.Kino.DB.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import de.fhdw.Kino.DB.domain.Auffuehrung;

public interface AuffuehrungRepository extends JpaRepository<Auffuehrung, Long> { }