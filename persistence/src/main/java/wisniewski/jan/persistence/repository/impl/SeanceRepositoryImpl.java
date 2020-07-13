package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.*;
import wisniewski.jan.persistence.repository.SeanceRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.time.LocalDateTime;
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
    public Optional<Seance> isUniqueSeance(Seance seance) {
        var sql = "select * from seances where movie_id = :movie_id AND cinema_room_id = :cinema_room_id AND date_time = :date_time";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("movie_id", seance.getMovieId())
                        .bind("cinema_room_id", seance.getCinemaRoomId())
                        .bind("date_time", seance.getDateTime())
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
    public Boolean isMovieDisplayed(Seance seance) {
        var sql = """
                select * from movies where id = :movie_id
                """;
        Optional<Movie> movieOp = dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("movie_id", seance.getMovieId())
                        .mapToBean(Movie.class)
                        .findFirst()
                );
        Movie movie = movieOp.orElseThrow(() -> new IllegalStateException(""));
        return seance.getDateTime().isAfter(movie.getDateFrom()) && seance.getDateTime().isBefore(movie.getDateTo());
    }

    @Override
    public List<Seance> findByMovie(Movie movie) {
        var sql = """
                select * from seances where movie_id = :movieId and date_time > :date
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("movieId", movie.getId())
                        .bind("date", LocalDateTime.now())
                        .mapToBean(Seance.class)
                        .list()
                );
    }

    @Override
    public List<Seance> findByCity(City city) {
        var sql = """
                select s.id, s.movie_id,s.cinema_room_id,s.date_time from seances s join cinema_rooms cr on s.cinema_room_id = cr.id join cinemas c on cr.cinema_id = c.id
                where date_time > :date and c.city_id = :city_id;
                   """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("date", LocalDateTime.now())
                        .bind("city_id", city.getId())
                        .mapToBean(Seance.class)
                        .list()
                );
    }

    @Override
    public List<Seance> findByCinema(Cinema cinema) {
        var sql = """
                select s.id,s.movie_id,s.cinema_room_id,s.date_time from seances s join cinema_rooms cr on s.cinema_room_id = cr.id where date_time > now() and cr.cinema_id = :cinemaId;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("cinemaId", cinema.getId())
                        .mapToBean(Seance.class)
                        .list()
                );
    }

    @Override
    public List<Seance> findByPhrase(String phrase) {
        var sql = """
                select s.id,s.movie_id,s.cinema_room_id,s.date_time 
                from seances s
                join movies m on s.movie_id = m.id
                join cinema_rooms cr on s.cinema_room_id = cr.id
                join cinemas c on cr.cinema_id = c.id
                join cities ct on c.city_id = ct.id
                where title LIKE '%' || :phrase || '%' or c.name LIKE '%' || :phrase || '%' or ct.name LIKE '%' || :phrase || '%'
                and s.date_time > now();
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("phrase",phrase)
                        .mapToBean(Seance.class)
                        .list()
                );
    }
}
