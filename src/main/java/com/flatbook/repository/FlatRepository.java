package com.flatbook.repository;

import com.flatbook.entity.Flat;
import com.flatbook.entity.Flat.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Long> {

    List<Flat> findByStatus(Status status);

    List<Flat> findByCityIgnoreCaseAndStatus(String city, Status status);

    @Query("SELECT f FROM Flat f WHERE f.status = :status " +
           "AND (:city IS NULL OR LOWER(f.city) = LOWER(:city)) " +
           "AND (:bhkType IS NULL OR f.bhkType = :bhkType) " +
           "AND (:minPrice IS NULL OR f.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR f.price <= :maxPrice)")
    List<Flat> searchFlats(@Param("city") String city,
                           @Param("bhkType") String bhkType,
                           @Param("minPrice") BigDecimal minPrice,
                           @Param("maxPrice") BigDecimal maxPrice,
                           @Param("status") Status status);

    long countByStatus(Status status);
}
