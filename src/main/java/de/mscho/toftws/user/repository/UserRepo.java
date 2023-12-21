package de.mscho.toftws.user.repository;

import de.mscho.toftws.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByAuthToken(String authToken);
    Optional<User> findByUsername(String username);
    void deleteUserByUsername(String username);
}
