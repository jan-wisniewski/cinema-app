package wisniewski.jan.persistence.repository.impl;

import org.jdbi.v3.core.result.ResultIterable;
import wisniewski.jan.persistence.connection.DbConnection;
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
               select cinema_rooms.id as id,
               cinema_rooms.name as name,
               cinema_rooms.cinema_id as cinema_id,
               cinema_rooms.rows_nums as rows_number,
               cinema_rooms.places as places
               from cinema_rooms JOIN cinemas ON cinema_rooms.cinema_id = cinemas.id where cinemas.city_id = :city_id;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                .createQuery(sql)
                .bind("city_id",cityId)
                .mapTo(CinemaRoom.class)
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
}
