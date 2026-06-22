package com.inditex.pricing.infrastructure.outbound.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query("SELECT p FROM PriceEntity p WHERE p.brandId = :brandId " +
            "AND p.productId = :productId " +
            "AND :applicationDate BETWEEN p.startDate AND p.endDate " +
            "ORDER BY p.priority DESC LIMIT 1")
    Optional<PriceEntity> findApplicablePrice(
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}