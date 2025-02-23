package de.fhdw.Kino.App.repository;

import de.fhdw.Kino.App.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> { }