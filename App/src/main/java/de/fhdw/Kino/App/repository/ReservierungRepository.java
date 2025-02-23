package de.fhdw.Kino.App.repository;

import de.fhdw.Kino.App.domain.Reservierung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservierungRepository extends JpaRepository<Reservierung, Long> { }