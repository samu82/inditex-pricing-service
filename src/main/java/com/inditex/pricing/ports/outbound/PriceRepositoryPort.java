package com.inditex.pricing.ports.outbound;

import com.inditex.pricing.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepositoryPort {
    Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}