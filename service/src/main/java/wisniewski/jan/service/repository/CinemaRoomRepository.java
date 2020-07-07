package wisniewski.jan.service.repository;

import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.service.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CinemaRoomRepository extends CrudRepository<CinemaRoom, Integer> {
    Optional<CinemaRoom> findByNameAndCinemaId(String name, Integer cinemaId);

    List<CinemaRoom> findByCityId(Integer cityId);

    List<CinemaRoom> findByCinemaId(Integer cinemaId);

    Integer deleteAllByCinemaId (Cinema cinema);

    List<CinemaRoom> findByMovieId(Integer movieId);
}
