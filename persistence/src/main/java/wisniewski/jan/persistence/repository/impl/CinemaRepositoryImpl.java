package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.Cinema;
import wisniewski.jan.persistence.repository.CinemaRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.Optional;

public class CinemaRepositoryImpl extends AbstractCrudRepository<Cinema, Integer> implements CinemaRepository {

    private final DbConnection dbConnection;

    public CinemaRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<Cinema> findByName(String name) {
        String SQL = "SELECT * FROM cinemas where name = :name";
        var foundCinema = dbConnection
                .getJdbi()
                .withHandle(h -> h.createQuery(SQL)
                        .bind("name", name)
                        .mapToBean(Cinema.class)
                        .findFirst()
                );
        return foundCinema;
    }
}
