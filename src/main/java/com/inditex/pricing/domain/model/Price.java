package com.inditex.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa el precio final aplicable a un producto de una cadena
 * en un rango de fechas determinado.
 */
public record Price(
        Long brandId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer priceList,
        Long productId,
        Integer priority,
        BigDecimal price,
        String currency
) {}