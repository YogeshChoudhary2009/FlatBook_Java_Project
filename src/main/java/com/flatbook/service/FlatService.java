package com.flatbook.service;

import com.flatbook.entity.Flat;
import com.flatbook.entity.Flat.Status;
import com.flatbook.repository.FlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlatService {

    private final FlatRepository flatRepository;

    public FlatService(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    public List<Flat> getAllAvailableFlats() {
        return flatRepository.findByStatus(Status.AVAILABLE);
    }

    public List<Flat> search(String city, String bhkType, BigDecimal minPrice, BigDecimal maxPrice) {
        return flatRepository.searchFlats(city, bhkType, minPrice, maxPrice, Status.AVAILABLE);
    }

    public Optional<Flat> findById(Long id) {
        return flatRepository.findById(id);
    }

    public List<Flat> getAllFlats() {
        return flatRepository.findAll();
    }

    @Transactional
    public Flat save(Flat flat) {
        flat.setUpdatedAt(LocalDateTime.now());
        return flatRepository.save(flat);
    }

    @Transactional
    public void delete(Long id) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        if (flat.getStatus() == Status.BOOKED) {
            throw new RuntimeException("Cannot delete a booked flat. Change status first.");
        }
        flatRepository.delete(flat);
    }

    @Transactional
    public void updateStatus(Long id, Status status) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        flat.setStatus(status);
        flat.setUpdatedAt(LocalDateTime.now());
        flatRepository.save(flat);
    }

    public long countAvailable() {
        return flatRepository.countByStatus(Status.AVAILABLE);
    }

    public long countAll() {
        return flatRepository.count();
    }
}
