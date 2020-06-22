package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.dto.CreateMovieDto;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.repository.MovieRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.Optional;

public class MovieRepositoryImpl extends AbstractCrudRepository<Movie, Integer> implements MovieRepository {
    private DbConnection dbConnection;

    public MovieRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<Movie> isUniqueMovie(CreateMovieDto movieDto) {
        var SQL = "select * from movies where title=:title and genre=:genre and date_from=:dateFrom and date_to=:dateTo";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(SQL)
                        .bind("title", movieDto.getTitle())
                        .bind("genre", movieDto.getGenre())
                        .bind("dateFrom", movieDto.getDateFrom())
                        .bind("dateTo", movieDto.getDateTo())
                .mapToBean(Movie.class)
                .findFirst());
    }
}
