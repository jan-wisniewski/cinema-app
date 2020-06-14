package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.User;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
