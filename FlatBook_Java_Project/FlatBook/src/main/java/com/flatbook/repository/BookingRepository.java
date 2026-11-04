package com.flatbook.repository;

import com.flatbook.entity.Booking;
import com.flatbook.entity.Booking.Status;
import com.flatbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingId(String bookingId);

    List<Booking> findByUserOrderByCreatedAtDesc(User user);

    List<Booking> findByStatus(Status status);

    long countByStatus(Status status);

    List<Booking> findAllByOrderByCreatedAtDesc();
}
