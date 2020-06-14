package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.persistence.repository.UserRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

public class UserRepositoryImpl extends AbstractCrudRepository<User,Integer> implements UserRepository {
    public UserRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
    }
}
