package de.fhdw.Kino.DB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import de.fhdw.Kino.DB.model.Auffuehrung;

public interface AuffuehrungRepository extends JpaRepository<Auffuehrung, Long> { }