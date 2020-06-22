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
