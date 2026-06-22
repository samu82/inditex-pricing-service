package com.inditex.pricing.domain.service; // <- ¡ESTA LÍNEA ES CLAVE!

import com.inditex.pricing.domain.exception.PriceNotFoundException;
import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.ports.inbound.GetApplicablePriceUseCase;
import com.inditex.pricing.ports.outbound.PriceRepositoryPort;

import java.time.LocalDateTime;

public class PriceService implements GetApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    // Inyección por constructor clásica de Java puro (sin @Autowired)
    public PriceService(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Price execute(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceRepositoryPort.findApplicablePrice(brandId, productId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(brandId, productId, applicationDate));
    }
}