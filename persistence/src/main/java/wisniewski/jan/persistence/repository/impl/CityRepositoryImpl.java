package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.City;
import wisniewski.jan.persistence.repository.CityRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.Optional;

public class CityRepositoryImpl extends AbstractCrudRepository<City, Integer> implements CityRepository {

    private DbConnection dbConnection;

    public CityRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }


    @Override
    public Optional<City> findByName(String cityName) {
        var sql = """
                select * from cities where name = :cityName;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("cityName", cityName)
                        .mapToBean(City.class)
                        .findFirst()
                );
    }
}
