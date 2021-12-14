package de.mscho.toftws.repo;

import de.mscho.toftws.entity.Birthday;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface BirthdayRepo extends CrudRepository<Birthday, Long> {

    List<Birthday> findBirthdayByDateBetween(LocalDate begin, LocalDate end);
}
