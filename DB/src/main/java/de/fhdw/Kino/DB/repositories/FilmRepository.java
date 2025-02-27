package de.fhdw.Kino.DB.repositories;

import de.fhdw.Kino.DB.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> { }