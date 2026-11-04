package com.flatbook.service;

import com.flatbook.entity.*;
import com.flatbook.repository.BookingRepository;
import com.flatbook.repository.FlatRepository;
import com.flatbook.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlatRepository flatRepository;
    private final PaymentRepository paymentRepository;
    private final AtomicLong sequence = new AtomicLong(1);

    public BookingService(BookingRepository bookingRepository,
                          FlatRepository flatRepository,
                          PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.flatRepository = flatRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Booking createBooking(User user, Long flatId, LocalDate startDate,
                                 LocalDate endDate, Integer occupants, String specialRequests) {

        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));

        if (flat.getStatus() != Flat.Status.AVAILABLE) {
            throw new RuntimeException("Flat is not available for booking");
        }

        // Generate unique booking ID
        String bookingId = "FB-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%04d", sequence.getAndIncrement());

        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setUser(user);
        booking.setFlat(flat);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setOccupants(occupants != null ? occupants : 1);
        booking.setSpecialRequests(specialRequests);
        booking.setTotalAmount(flat.getPrice());
        booking.setStatus(Booking.Status.PENDING);

        // Temporarily mark flat as BOOKED (will be released if payment fails)
        flat.setStatus(Flat.Status.BOOKED);
        flatRepository.save(flat);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Payment processPayment(String bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new RuntimeException("Booking is not in pending state");
        }

        // Simulate successful payment (in real app integrate Razorpay / Stripe)
        String paymentId = "PAY-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + System.currentTimeMillis() % 100000;

        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : "DEMO");
        payment.setStatus(Payment.Status.SUCCESS);
        payment.setTransactionRef("TXN-" + System.currentTimeMillis());
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Confirm booking
        booking.setStatus(Booking.Status.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return payment;
    }

    @Transactional
    public void cancelBooking(String bookingId, String reason) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.Status.CONFIRMED && booking.getStatus() != Booking.Status.PENDING) {
            throw new RuntimeException("Only confirmed or pending bookings can be cancelled");
        }

        booking.setStatus(Booking.Status.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Release the flat
        Flat flat = booking.getFlat();
        flat.setStatus(Flat.Status.AVAILABLE);
        flatRepository.save(flat);
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Booking> findByBookingId(String bookingId) {
        return bookingRepository.findByBookingId(bookingId);
    }

    public long countConfirmed() {
        return bookingRepository.countByStatus(Booking.Status.CONFIRMED);
    }

    public long countAll() {
        return bookingRepository.count();
    }
}
