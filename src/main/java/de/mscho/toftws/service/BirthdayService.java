package de.mscho.toftws.service;

import de.mscho.toftws.entity.Birthday;
import de.mscho.toftws.exception.NameFormatException;
import de.mscho.toftws.repo.BirthdayRepo;
import de.mscho.toftws.util.DateTimeUtil;
import de.mscho.toftws.util.RegexUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BirthdayService {

    private final BirthdayRepo birthdayRepo;

    public BirthdayService(BirthdayRepo birthdayRepo) {
        this.birthdayRepo = birthdayRepo;
    }

    public Birthday addNewBirthday(String firstname, String surname, String dateString) throws NameFormatException {

        if(!RegexUtil.areValidNames(firstname, surname))
            throw new NameFormatException(firstname, surname);

        LocalDate date = LocalDate.parse(dateString, DateTimeUtil.DTF_YEAR_MONTH_DAY);

        return birthdayRepo.save(new Birthday(firstname, surname, date));
    }
}
