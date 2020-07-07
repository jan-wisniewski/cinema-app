package wisniewski.jan.service.repository.impl;

import wisniewski.jan.persistence.connection.DbConnection;
import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.repository.AdminRepository;
import wisniewski.jan.service.repository.generic.AbstractCrudRepository;

public class AdminRepositoryImpl extends AbstractCrudRepository<User, Integer> implements AdminRepository {
    public AdminRepositoryImpl(DbConnection dbConnection) {
        super(dbConnection);
    }
}
