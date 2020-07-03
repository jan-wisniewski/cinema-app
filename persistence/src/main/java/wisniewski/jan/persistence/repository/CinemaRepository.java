package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.Optional;

public interface CinemaRepository extends CrudRepository<Cinema, Integer> {
    Optional<Cinema> findByName(String name);

    Optional<Cinema> findByCityId(Integer cityId);
}
