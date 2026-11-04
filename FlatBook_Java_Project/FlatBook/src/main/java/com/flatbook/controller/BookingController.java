package com.flatbook.controller;

import com.flatbook.entity.Booking;
import com.flatbook.entity.Payment;
import com.flatbook.entity.User;
import com.flatbook.service.BookingService;
import com.flatbook.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/bookings/new/{flatId}")
    public String showBookingForm(@PathVariable Long flatId, Model model) {
        model.addAttribute("flatId", flatId);
        return "booking-form";
    }

    @PostMapping("/bookings/create")
    public String createBooking(@RequestParam Long flatId,
                                @RequestParam String startDate,
                                @RequestParam(required = false) String endDate,
                                @RequestParam(defaultValue = "1") Integer occupants,
                                @RequestParam(required = false) String specialRequests,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes ra) {
        try {
            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = (endDate != null && !endDate.isBlank()) ? LocalDate.parse(endDate) : null;

            Booking booking = bookingService.createBooking(user, flatId, start, end, occupants, specialRequests);
            return "redirect:/payment/" + booking.getBookingId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/flats/" + flatId;
        }
    }

    @GetMapping("/payment/{bookingId}")
    public String showPayment(@PathVariable String bookingId, Model model) {
        Booking booking = bookingService.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        model.addAttribute("booking", booking);
        return "payment";
    }

    @PostMapping("/payment/process")
    public String processPayment(@RequestParam String bookingId,
                                 @RequestParam String paymentMethod,
                                 RedirectAttributes ra) {
        try {
            Payment payment = bookingService.processPayment(bookingId, paymentMethod);
            ra.addFlashAttribute("success", "Payment successful! Booking confirmed.");
            return "redirect:/booking-success/" + bookingId;
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/payment/" + bookingId;
        }
    }

    @GetMapping("/booking-success/{bookingId}")
    public String bookingSuccess(@PathVariable String bookingId, Model model) {
        Booking booking = bookingService.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        model.addAttribute("booking", booking);
        return "booking-success";
    }

    @GetMapping("/my-bookings")
    public String myBookings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("bookings", bookingService.getUserBookings(user));
        return "my-bookings";
    }

    @PostMapping("/bookings/cancel")
    public String cancelBooking(@RequestParam String bookingId,
                                @RequestParam(required = false) String reason,
                                RedirectAttributes ra) {
        try {
            bookingService.cancelBooking(bookingId, reason != null ? reason : "User cancelled");
            ra.addFlashAttribute("success", "Booking cancelled successfully. Flat is now available.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-bookings";
    }
}
