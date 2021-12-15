package de.mscho.toftws.controller;

import de.mscho.toftws.entity.Birthday;
import de.mscho.toftws.service.BirthdayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/get/between")
    @ResponseBody
    public List<Birthday> getBirthdaysBetween(
            @RequestParam(name = "begin") String beginString,
            @RequestParam(name = "end") String endString) {
        return birthdayService.getBirthdaysBetween(beginString, endString);
    }

    @GetMapping("/get/from")
    @ResponseBody
    public List<Birthday> getBirthdaysFrom(
            @RequestParam(name="from") String fromString) {
        return birthdayService.getBirthdayFrom(fromString);
    }
}
