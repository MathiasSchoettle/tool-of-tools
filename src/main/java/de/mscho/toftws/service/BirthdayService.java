package de.mscho.toftws.service;

import de.mscho.toftws.entity.Birthday;
import de.mscho.toftws.exception.NameFormatException;
import de.mscho.toftws.repo.BirthdayRepo;
import de.mscho.toftws.util.DateTimeUtil;
import de.mscho.toftws.util.RegexUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public List<Birthday> getBirthdaysBetween(String beginString, String endString) {

        LocalDate begin = LocalDate.parse(beginString, DateTimeUtil.DTF_YEAR_MONTH_DAY);
        LocalDate end = LocalDate.parse(endString, DateTimeUtil.DTF_YEAR_MONTH_DAY);

        return birthdayRepo.findBirthdayByDateBetween(begin, end);
    }

    public List<Birthday> getBirthdayFrom(String fromString) {
        LocalDate from = LocalDate.parse(fromString, DateTimeUtil.DTF_YEAR_MONTH_DAY);
        return birthdayRepo.findBirthdayByDate(from);
    }
}
