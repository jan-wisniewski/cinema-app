package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.persistence.repository.UserRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

import java.util.Optional;

public class UserRepositoryImpl extends AbstractCrudRepository<User, Integer> implements UserRepository {

    private final DbConnection dbConnection;


    public UserRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
        this.dbConnection = dbConnection;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        var sql = """
                select * from users where username = :username
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("username", username)
                        .mapToBean(User.class)
                        .findFirst()
                );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var sql = """
                select * from users where email = :email;
                """;
        return dbConnection
                .getJdbi()
                .withHandle(handle -> handle
                        .createQuery(sql)
                        .bind("email", email)
                        .mapToBean(User.class)
                        .findFirst()
                );
    }
}
