package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.service.repository.MovieRepository;
import wisniewski.jan.service.repository.SeanceRepository;
import wisniewski.jan.service.exception.UserDataServiceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final MovieRepository movieRepository;

    public Optional<Seance> getSeanceById(Integer seanceId) {
        return seanceRepository.findById(seanceId);
    }

    public List<Seance> getSeancesListByCinemaRooms(List<CinemaRoom> cinemaRooms) {
        return seanceRepository
                .findSeancesByCinemaRooms(cinemaRooms);
    }

    public String showAll() {
        return seanceRepository.findAll()
                .stream()
                .map(seance -> seance.getId() + ". " +
                        movieRepository.findById(seance.getMovieId())
                                .orElseThrow(() -> new UserDataServiceException("FAILED!"))
                                .getTitle()
                )
                .collect(Collectors.joining("\n"));
    }

    public List<Seance> getAll() {
        return seanceRepository.findAll();
    }

    public List<Seance> findSeancesFromDateAtCinemaRoom(Integer cinemaRoomId) {
        return seanceRepository.findFutureSeancesAtCinemaRoom(cinemaRoomId);
    }

    public List<Seance> findByMovie(Movie movie) {
        return seanceRepository.findByMovie(movie);
    }

    public List<Seance> findByPhrase(String phrase) {
        return seanceRepository.findByPhrase(phrase);
    }

    public Optional<Seance> findById(Integer seanceId) {
        return seanceRepository.findById(seanceId);
    }
}
