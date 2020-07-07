package wisniewski.jan.service.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.repository.UserRepository;
import wisniewski.jan.service.repository.generic.AbstractCrudRepository;

public class UserRepositoryImpl extends AbstractCrudRepository<User,Integer> implements UserRepository {
    public UserRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
    }
}
