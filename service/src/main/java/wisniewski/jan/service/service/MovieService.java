package wisniewski.jan.service.service;

import lombok.RequiredArgsConstructor;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.repository.MovieRepository;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.service.dto.CreateMovieDto;
import wisniewski.jan.service.exception.AdminServiceException;
import wisniewski.jan.service.exception.MovieServiceException;
import wisniewski.jan.service.mappers.Mapper;
import wisniewski.jan.service.validator.CreateMovieDtoValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final SeanceRepository seanceRepository;

    public Optional<Movie> editMovie(Movie movie) {
        return movieRepository.update(movie);
    }

    public Integer addMovie(CreateMovieDto movieDto) {
        if (movieDto == null) {
            throw new AdminServiceException("movieDto is null");
        }
        var validator = new CreateMovieDtoValidator();
        var errors = validator.validate(movieDto);
        if (!errors.isEmpty()) {
            String errorsMessage = errors
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + " : " + e.getValue())
                    .collect(Collectors.joining("\n"));
            throw new AdminServiceException("Add movie errors: " + errorsMessage);
        }

        var movieToAdd = Mapper.fromMovieDtoToMovie(movieDto);

        if (movieRepository.isUniqueMovie(movieToAdd).isPresent()) {
            throw new AdminServiceException("This movie with that dateFrom and dateTo is already on db");
        }
        var addedMovie = movieRepository
                .add(movieToAdd)
                .orElseThrow(() -> new AdminServiceException("cannot insert to db"));
        return addedMovie.getId();
    }


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

    public Integer deleteMovie(Movie movie) {
        if (!seanceRepository.findByMovie(movie).isEmpty()) {
            System.out.println("Can't delete movie! Movie will be displayed!");
            return 0;
        }
        return (movieRepository.deleteById(movie.getId())) ? 1 : 0;
    }

}
