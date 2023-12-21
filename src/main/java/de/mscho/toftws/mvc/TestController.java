package de.mscho.toftws.mvc;

import de.mscho.toftws.user.entity.User;
import de.mscho.toftws.user.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional
public class TestController {

    private final UserService userService;

    @ModelAttribute("users")
    public List<User> users() {
        return userService.getAllUsers();
    }

    @ModelAttribute("user")
    public UserDto user() {
        return new UserDto();
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/home")
    public String submit(@ModelAttribute @Valid UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
        } else {
            userService.createUser(user.getUsername(), user.getToken());
        }
        model.addAttribute("users", users());

        return "/home";
    }

    @GetMapping("/home/delete")
    public String delete(@RequestParam String username) {
        userService.deleteUser(username);
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
