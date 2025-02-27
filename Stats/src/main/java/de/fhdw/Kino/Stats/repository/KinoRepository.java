package de.fhdw.Kino.Stats.repository;

import de.fhdw.Kino.Stats.model.KinoDocument;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KinoRepository extends CouchbaseRepository<KinoDocument, Long> {
}