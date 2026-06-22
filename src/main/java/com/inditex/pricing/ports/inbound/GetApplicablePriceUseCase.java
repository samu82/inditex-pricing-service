package com.inditex.pricing.ports.inbound;

import com.inditex.pricing.domain.model.Price;
import java.time.LocalDateTime;

public interface GetApplicablePriceUseCase {
    Price execute(Long brandId, Long productId, LocalDateTime applicationDate);
}