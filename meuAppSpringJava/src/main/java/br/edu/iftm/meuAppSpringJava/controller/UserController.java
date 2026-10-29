package br.edu.iftm.meuAppSpringJava.controller;

import java.util.Locale;

import br.edu.iftm.meuAppSpringJava.model.User;
import br.edu.iftm.meuAppSpringJava.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/registerUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "user/registerUser";
        }

        user.setEmail(user.getEmail().toLowerCase(Locale.ROOT));
        try {
            userService.register(user);
        } catch (IllegalArgumentException exception) {
            result.rejectValue("email", "user.email.duplicate", exception.getMessage());
            return "user/registerUser";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "user/accessDeniedPage";
    }
}