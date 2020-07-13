package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.repository.MovieRepository;
import wisniewski.jan.service.exception.MovieServiceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public String showTitle(Integer movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new MovieServiceException("Failed")).getTitle();
    }

    public String showAll() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> movie.getId() + ". " + movie.getTitle())
                .collect(Collectors.joining("\n"));
    }

    public Optional<Movie> findById(Integer movieId) {
        return movieRepository.findById(movieId);
    }

    public List<Movie> getAll (){
        return movieRepository.findAll();
    }

}
