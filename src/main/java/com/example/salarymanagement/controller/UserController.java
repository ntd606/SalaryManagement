package com.example.salarymanagement.controller;

import com.example.salarymanagement.entity.User;
import com.example.salarymanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("listUsers", userService.getAllUsers());

        if (id != null) {
            User user = userService.getUserById(id).orElse(new User());
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        return "index";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("listUsers", userService.getAllUsers());
            return "index";
        }

        try {
            boolean isNew = (user.getId() == null);
            userService.saveUser(user);
            if (isNew) {
                redirectAttributes.addFlashAttribute("successMessage", "User created successfully");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error while creating User: " + e.getMessage());
            model.addAttribute("listUsers", userService.getAllUsers());
            return "index";
        }
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("id", id);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        return "redirect:/";
    }
}