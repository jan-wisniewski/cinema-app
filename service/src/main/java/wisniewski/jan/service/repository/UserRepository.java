package wisniewski.jan.service.repository;

import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.repository.generic.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
