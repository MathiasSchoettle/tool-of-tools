package de.mscho.toftws.controller;

import de.mscho.toftws.entity.Birthday;
import de.mscho.toftws.exception.NameFormatException;
import de.mscho.toftws.repo.BirthdayRepo;
import de.mscho.toftws.service.BirthdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            @RequestParam String dateString)
    {
        return birthdayService.addNewBirthday(firstname, surname, dateString);
    }

    @GetMapping(path = "/get")
    @ResponseBody
    public List<Birthday> getBirthdays(
            @RequestParam(name = "begin") String beginString,
            @RequestParam(name = "end") String endString)
    {
        return birthdayService.getBirthdays(beginString, endString);
    }

}
