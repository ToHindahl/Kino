package de.fhdw.Kino.DB.repository;

import de.fhdw.Kino.DB.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> { }