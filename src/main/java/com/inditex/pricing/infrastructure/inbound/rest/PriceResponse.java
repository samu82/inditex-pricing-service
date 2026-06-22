package com.inditex.pricing.infrastructure.inbound.rest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(
        Long productId,
        Long brandId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency
) {}