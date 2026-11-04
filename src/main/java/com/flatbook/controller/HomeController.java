package com.flatbook.controller;

import com.flatbook.entity.Flat;
import com.flatbook.entity.User;
import com.flatbook.service.FlatService;
import com.flatbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class HomeController {

    private final FlatService flatService;
    private final UserService userService;

    public HomeController(FlatService flatService, UserService userService) {
        this.flatService = flatService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Flat> flats = flatService.getAllAvailableFlats();
        model.addAttribute("flats", flats);
        model.addAttribute("cities", List.of("Bangalore", "Mumbai", "Delhi", "Pune", "Hyderabad", "Chennai"));
        return "index";
    }

    @GetMapping("/flats")
    public String searchFlats(@RequestParam(required = false) String city,
                              @RequestParam(required = false) String bhkType,
                              @RequestParam(required = false) BigDecimal minPrice,
                              @RequestParam(required = false) BigDecimal maxPrice,
                              Model model) {
        List<Flat> flats = flatService.search(city, bhkType, minPrice, maxPrice);
        model.addAttribute("flats", flats);
        model.addAttribute("city", city);
        model.addAttribute("bhkType", bhkType);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("cities", List.of("Bangalore", "Mumbai", "Delhi", "Pune", "Hyderabad", "Chennai"));
        return "flats";
    }

    @GetMapping("/flats/{id}")
    public String flatDetail(@PathVariable Long id, Model model) {
        Flat flat = flatService.findById(id)
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        model.addAttribute("flat", flat);
        return "flat-detail";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user,
                           BindingResult result,
                           RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.register(user);
            ra.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
