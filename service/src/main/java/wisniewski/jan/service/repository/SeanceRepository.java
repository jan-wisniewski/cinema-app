package wisniewski.jan.service.repository;

import wisniewski.jan.persistence.model.*;
import wisniewski.jan.service.repository.generic.CrudRepository;
import wisniewski.jan.service.dto.CreateSeanceDto;

import java.util.List;
import java.util.Optional;

public interface SeanceRepository extends CrudRepository<Seance, Integer> {
    Optional<Seance> isUniqueSeance(CreateSeanceDto seanceDto);

    List<Seance> findSeancesByCinemaRooms(List<CinemaRoom> cinemaRooms);

    List<Seance> findFutureSeancesAtCinemaRoom(Integer cinemaRoomId);

    Boolean isMovieDisplayed(CreateSeanceDto seanceDto);

    List<Seance> findByMovie(Movie movie);

    List<Seance> findByCinema(Cinema cinema);

    List<Seance> findByCity (City city);

    List<Seance> findByPhrase(String phrase);
}
