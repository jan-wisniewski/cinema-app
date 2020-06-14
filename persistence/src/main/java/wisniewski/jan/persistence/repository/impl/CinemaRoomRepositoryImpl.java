package wisniewski.jan.persistence.repository.impl;

import org.jdbi.v3.core.result.ResultIterable;
import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.Optional;

public class CinemaRoomRepositoryImpl extends AbstractCrudRepository<CinemaRoom, Integer> implements CinemaRoomRepository {

    private DbConnection dbConnection;

    public CinemaRoomRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<CinemaRoom> findByNameAndCinemaId(String name, Integer cinemaId) {
        var SQL = "select * from cinema_rooms where name=:name and cinemaId=:cinemaId";
        var foundCinemaRoom = dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(SQL)
                        .bind("name", name)
                        .bind("cinemaId", cinemaId)
                        .mapToBean(CinemaRoom.class)
                        .findFirst()
                );
        return foundCinemaRoom;
    }
}
