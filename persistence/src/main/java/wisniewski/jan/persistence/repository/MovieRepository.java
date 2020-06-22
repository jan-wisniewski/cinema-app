package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.dto.CreateMovieDto;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    Optional<Movie> isUniqueMovie(CreateMovieDto movieDto);
}
