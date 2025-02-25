package de.fhdw.Kino.DB.repositories;

import de.fhdw.Kino.DB.domain.Reservierung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservierungRepository extends JpaRepository<Reservierung, Long> { }