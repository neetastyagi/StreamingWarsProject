package edu.gatech.streamingwars.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private List<Role> roles;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        roles = userService.getRoles();
        List<Role> roleList = new ArrayList<>();
        for (Role role : roles) {
            if (!role.getName().equalsIgnoreCase("admin")) {
                roleList.add(role);
            }
        }
        model.addAttribute("appUser", new AppUser());
        model.addAttribute("roles", roleList);
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(AppUser appUser) {
        try {
            userService.registerUser(appUser);
        } catch (Exception e) {
            return "redirect:/registration?error";
        }
        return "redirect:/registration?success";
    }

    @GetMapping("/")
    public String redirectOnRole(Authentication auth) {
        String page = null;
        if (auth.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("account")) {
            page = "redirect:/account";
        } else if (auth.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("studio")) {
            page = "redirect:/studio";
        } else if (auth.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("streaming service")) {
            page = "redirect:/streamingservice";
        } else if (auth.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("admin")) {
            page = "redirect:/admin";
        }
        return page;
    }
}
