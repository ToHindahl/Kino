package de.fhdw.Kino.DB.repository;

import de.fhdw.Kino.DB.model.Reservierung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservierungRepository extends JpaRepository<Reservierung, Long> { }