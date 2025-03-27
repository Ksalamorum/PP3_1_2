package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;

@Controller
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String allUsersPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/admin/show")
    public String userById(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user";
    }

    @GetMapping("/admin/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "new";
    }

    @PostMapping("/admin/addUser")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAll());
            return "new";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editUserById(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findAll());
        return "edit";
    }

    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.findAll());
            return "edit";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}