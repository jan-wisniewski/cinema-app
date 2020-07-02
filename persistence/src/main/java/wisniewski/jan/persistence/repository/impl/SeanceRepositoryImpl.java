package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.dto.CreateSeanceDto;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.model.Movie;
import wisniewski.jan.persistence.model.Seance;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SeanceRepositoryImpl extends AbstractCrudRepository<Seance, Integer> implements SeanceRepository {

    private final DbConnection dbConnection;

    public SeanceRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<Seance> add(Seance item) {
        return super.add(item);
    }

    @Override
    public Optional<Seance> isUniqueSeance(CreateSeanceDto seanceDto) {
        var sql = "select * from seances where movie_id = :movie_id AND cinema_room_id = :cinema_room_id AND date_time = :date_time";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("movie_id", seanceDto.getMovieId())
                        .bind("cinema_room_id", seanceDto.getCinemaRoomId())
                        .bind("date_time", seanceDto.getDateTime())
                        .mapToBean(Seance.class)
                        .findFirst()
                );
    }

    @Override
    public List<Seance> findSeancesByCinemaRooms(List<CinemaRoom> cinemaRooms) {
        List<Integer> cinemaRoomsIds = cinemaRooms
                .stream()
                .map(CinemaRoom::getId)
                .collect(Collectors.toList());
        var SQL = """
                select * from seances where cinema_room_id IN (<cinemaRoomsIds>)
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle ->
                        handle.createQuery(SQL)
                                .bindList("cinemaRoomsIds", cinemaRoomsIds)
                                .mapToBean(Seance.class)
                                .list()
                );
    }

    @Override
    public List<Seance> findFutureSeancesAtCinemaRoom(Integer cinemaRoomId) {
        var sql = """
                select * from seances where date_time > now() AND cinema_room_id = :cinema_room_id;        
                        """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("cinema_room_id", cinemaRoomId)
                        .mapToBean(Seance.class)
                        .list()
                );
    }

    @Override
    public Boolean isMovieDisplayed(CreateSeanceDto seanceDto) {
        var sql = """
                select * from movies where id = :movie_id
                """;
        Optional<Movie> movieOp = dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("movie_id", seanceDto.getMovieId())
                        .mapToBean(Movie.class)
                        .findFirst()
                );
        Movie movie = movieOp.orElseThrow(() -> new IllegalStateException(""));
        return seanceDto.getDateTime().isAfter(movie.getDateFrom()) && seanceDto.getDateTime().isBefore(movie.getDateTo());
    }

}
