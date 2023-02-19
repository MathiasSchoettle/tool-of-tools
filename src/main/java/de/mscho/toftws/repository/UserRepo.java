package de.mscho.toftws.repository;

import de.mscho.toftws.entity.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
}
