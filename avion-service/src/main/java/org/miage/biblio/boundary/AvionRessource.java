package org.miage.biblio.boundary;

import org.miage.biblio.entity.Avion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="avion", path="avions")
public interface AvionRessource extends CrudRepository<Avion, String> {
}