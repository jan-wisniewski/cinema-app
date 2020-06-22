package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.dto.CreateCinemaRoomDto;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CinemaRoomRepository extends CrudRepository<CinemaRoom, Integer> {
    Optional<CinemaRoom> findByNameAndCinemaId(String name, Integer cinemaId);

    List<CinemaRoom> findByCinemaId(Integer cinemaId);
}
