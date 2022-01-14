package de.mscho.toftws.controller;

import de.mscho.toftws.entity.Birthday;
import de.mscho.toftws.service.BirthdayService;
import de.mscho.toftws.util.DateTimeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping(path = "/birthday")
public class BirthdayController {

    private final BirthdayService birthdayService;

    public BirthdayController(BirthdayService birthdayService) {
        this.birthdayService = birthdayService;
    }

    @PostMapping(path = "/add")
    @ResponseBody
    public Birthday addBirthday(
            @RequestParam String firstname,
            @RequestParam String surname,
            @RequestParam String dateString) {
        return birthdayService.addNewBirthday(firstname, surname, dateString);
    }

    @GetMapping(path = "/get/from")
    @ResponseBody
    public List<Birthday> getBirthdaysFrom(
            @RequestParam(name = "from") String fromString) {
        return birthdayService.getBirthdaysFrom(fromString);
    }

    @PostMapping(path = "/test")
    public void generateData() {

        Random r = new Random();
        String[] fn = {"James", "Robert", "John", "Michael", "William", "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth"};
        String[] sn = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};

        for(int i = 0; i < 50; i++) {
            String firstname = fn[r.nextInt(fn.length)];
            String surname = sn[r.nextInt(sn.length)];
            LocalDate date = LocalDate.now().plusDays(r.nextInt(365)).minusYears(r.nextInt(80) + 20);
            birthdayService.addNewBirthday(firstname, surname, DateTimeUtil.DTF_YEAR_MONTH_DAY.format(date));
        }
    }
}
