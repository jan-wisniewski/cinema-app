package wisniewski.jan.service.repository;

import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.service.repository.generic.CrudRepository;
import wisniewski.jan.service.dto.CreateMovieDto;

import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    Optional<Movie> isUniqueMovie(CreateMovieDto movieDto);
}
