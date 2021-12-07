package de.mscho.toftws.controller;

import de.mscho.toftws.entity.Birthday;
import de.mscho.toftws.repo.BirthdayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/birthday")
public class BirthdayController {

    @Autowired
    private BirthdayRepo birthdayRepo;

    @PostMapping(path = "/add")
    public void addBirthday(
            @RequestParam String name,
            @RequestParam Integer day,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        Birthday bd = new Birthday();
        bd.setName(name);
        bd.setDay(day);
        bd.setMonth(month);
        bd.setYear(year);

        birthdayRepo.save(bd);
    }

    @GetMapping(path = "/all")
    public @ResponseBody String getAllBirthdays() {
        Iterable<Birthday> birthdays = birthdayRepo.findAll();
        StringBuilder bob = new StringBuilder("names: \n");

        birthdays.forEach( bd -> bob.append(bd.getName()).append("\n"));

        return bob.toString();
    }
}
