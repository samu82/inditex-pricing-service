package com.inditex.pricing.domain.exception;

import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(Long brandId, Long productId, LocalDateTime applicationDate) {
        super(String.format("No se encontró ningún precio aplicable para la marca [%d], producto [%d] en la fecha [%s]",
                brandId, productId, applicationDate.toString()));
    }
}