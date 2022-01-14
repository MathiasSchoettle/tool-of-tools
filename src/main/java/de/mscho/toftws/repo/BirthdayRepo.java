package de.mscho.toftws.repo;

import de.mscho.toftws.entity.Birthday;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BirthdayRepo extends CrudRepository<Birthday, Long> {

    List<Birthday> findBirthdayByDateBetween(LocalDate begin, LocalDate end);

    List<Birthday> findBirthdayByDate(LocalDate date);

    @Query("SELECT bd FROM Birthday bd WHERE MONTH(bd.date) = MONTH(?1) AND DAYOFMONTH(bd.date) = DAYOFMONTH(?1)")
    List<Birthday> findBirthdayWithSameDate(LocalDate date);
}
