package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.*;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SeanceRepository extends CrudRepository<Seance, Integer> {
    Optional<Seance> isUniqueSeance(Seance seance);

    List<Seance> findSeancesByCinemaRooms(List<CinemaRoom> cinemaRooms);

    List<Seance> findFutureSeancesAtCinemaRoom(Integer cinemaRoomId);

    Boolean isMovieDisplayed(Seance seance);

    List<Seance> findByMovie(Movie movie);

    List<Seance> findByCinema(Cinema cinema);

    List<Seance> findByCity (City city);

    List<Seance> findByPhrase(String phrase);
}
