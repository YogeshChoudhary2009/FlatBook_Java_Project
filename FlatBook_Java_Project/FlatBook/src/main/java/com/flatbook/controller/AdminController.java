package com.flatbook.controller;

import com.flatbook.entity.Flat;
import com.flatbook.service.BookingService;
import com.flatbook.service.FlatService;
import com.flatbook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FlatService flatService;
    private final BookingService bookingService;
    private final UserService userService;

    public AdminController(FlatService flatService, BookingService bookingService, UserService userService) {
        this.flatService = flatService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalFlats", flatService.countAll());
        model.addAttribute("availableFlats", flatService.countAvailable());
        model.addAttribute("totalBookings", bookingService.countAll());
        model.addAttribute("confirmedBookings", bookingService.countConfirmed());
        model.addAttribute("totalUsers", userService.countUsers());
        return "admin/dashboard";
    }

    @GetMapping("/flats")
    public String manageFlats(Model model) {
        model.addAttribute("flats", flatService.getAllFlats());
        return "admin/flats";
    }

    @GetMapping("/flats/add")
    public String showAddFlat(Model model) {
        model.addAttribute("flat", new Flat());
        return "admin/flat-form";
    }

    @GetMapping("/flats/edit/{id}")
    public String showEditFlat(@PathVariable Long id, Model model) {
        Flat flat = flatService.findById(id)
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        model.addAttribute("flat", flat);
        return "admin/flat-form";
    }

    @PostMapping("/flats/save")
    public String saveFlat(@ModelAttribute Flat flat, RedirectAttributes ra) {
        try {
            if (flat.getId() == null) {
                flat.setStatus(Flat.Status.AVAILABLE);
            }
            flatService.save(flat);
            ra.addFlashAttribute("success", "Flat saved successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/flats";
    }

    @PostMapping("/flats/delete/{id}")
    public String deleteFlat(@PathVariable Long id, RedirectAttributes ra) {
        try {
            flatService.delete(id);
            ra.addFlashAttribute("success", "Flat deleted");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/flats";
    }

    @PostMapping("/flats/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        try {
            flatService.updateStatus(id, Flat.Status.valueOf(status));
            ra.addFlashAttribute("success", "Status updated");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/flats";
    }

    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users/toggle/{id}")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.toggleActive(id);
            ra.addFlashAttribute("success", "User status updated");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
