package wisniewski.jan.service.repository;

import wisniewski.jan.persistence.model.User;
import wisniewski.jan.service.repository.generic.CrudRepository;

public interface AdminRepository extends CrudRepository<User, Integer> {
}
