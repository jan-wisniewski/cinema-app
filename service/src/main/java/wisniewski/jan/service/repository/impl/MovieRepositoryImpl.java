package wisniewski.jan.service.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.service.dto.CreateMovieDto;
import wisniewski.jan.service.repository.MovieRepository;
import wisniewski.jan.service.repository.generic.AbstractCrudRepository;

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
