package wisniewski.jan.persistence.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.persistence.repository.AdminRepository;
import wisniewski.jan.persistence.repository.generic.AbstractCrudRepository;

public class AdminRepositoryImpl extends AbstractCrudRepository<User, Integer> implements AdminRepository {
    public AdminRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
    }
}
