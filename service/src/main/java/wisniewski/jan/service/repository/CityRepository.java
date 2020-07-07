package wisniewski.jan.service.repository;

import wisniewski.jan.persistence.model.City;
import wisniewski.jan.service.repository.generic.CrudRepository;

import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Integer> {
    Optional<City> findByName (String cityName);
}
