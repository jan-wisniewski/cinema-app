package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.CinemaRoom;
import wisniewski.jan.persistence.repository.CinemaRoomRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

public class CinemaRoomRepositoryImpl extends AbstractCrudRepository<CinemaRoom, Integer> implements CinemaRoomRepository {
    public CinemaRoomRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
    }
}
