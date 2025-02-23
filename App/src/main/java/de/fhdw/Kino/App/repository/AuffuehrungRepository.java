package de.fhdw.Kino.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import de.fhdw.Kino.App.domain.Auffuehrung;

public interface AuffuehrungRepository extends JpaRepository<Auffuehrung, Long> { }