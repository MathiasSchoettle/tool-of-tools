package de.mscho.toftws.repository.user;

import de.mscho.toftws.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByAuthToken(String authToken);
}
