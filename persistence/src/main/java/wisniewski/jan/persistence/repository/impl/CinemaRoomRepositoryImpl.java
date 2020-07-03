package wisniewski.jan.persistence.repository.impl;

import org.jdbi.v3.core.result.ResultIterable;
import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;

public class CinemaRoomRepositoryImpl extends AbstractCrudRepository<CinemaRoom, Integer> implements CinemaRoomRepository {

    private DbConnection dbConnection;

    public CinemaRoomRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<CinemaRoom> findByNameAndCinemaId(String name, Integer cinema_id) {
        var SQL = "select * from cinema_rooms where name=:name AND cinema_id=:cinema_id";
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(SQL)
                        .bind("name", name)
                        .bind("cinema_id", cinema_id)
                        .mapToBean(CinemaRoom.class)
                        .findFirst()
                );
    }

    @Override
    public List<CinemaRoom> findByCityId(Integer cityId) {
        var sql = """
                select cr.id, cr.name, cr.cinema_id, cr.rows_number, cr.places
                from cinema_rooms cr JOIN cinemas c ON cr.cinema_id = c.id
                where c.city_id = :city_id;
                 """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("city_id", cityId)
                        .mapToBean(CinemaRoom.class)
                        .list());
    }

    @Override
    public List<CinemaRoom> findByCinemaId(Integer cinemaId) {
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery("select * from cinema_rooms where cinema_id = :cinemaId")
                        .bind("cinemaId", cinemaId)
                        .mapToBean(CinemaRoom.class)
                        .list()
                );
    }

    @Override
    public List<CinemaRoom> findByMovieId(Integer movieId) {
        var sql = """
                select cr.id, name, cinema_id, rows_number, places
                from seances s join cinema_rooms cr on s.cinema_room_id = cr.id
                where movie_id = :movieId
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("movieId", movieId)
                        .mapToBean(CinemaRoom.class)
                        .list()
                );
    }

    @Override
    public Integer deleteAllByCinemaId(Cinema cinema) {
        var sql = """
                delete from cinema_rooms where cinema_id = :cinemaId;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createUpdate(sql)
                        .bind("cinemaId", cinema.getId())
                        .execute()
                );
    }
}
