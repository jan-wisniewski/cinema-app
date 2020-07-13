package wisniewski.jan.persistence.repository;

import wisniewski.jan.persistence.model.User;
import wisniewski.jan.persistence.repository.generic.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
