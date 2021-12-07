package de.mscho.toftws.repo;

import de.mscho.toftws.entity.Birthday;
import org.springframework.data.repository.CrudRepository;

public interface BirthdayRepo extends CrudRepository<Birthday, Long> {
}
