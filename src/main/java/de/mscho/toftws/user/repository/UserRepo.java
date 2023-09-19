package de.mscho.toftws.user.repository;

import de.mscho.toftws.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByAuthToken(String authToken);
}
