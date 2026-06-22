package com.inditex.pricing.infrastructure.outbound.db;

import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.ports.outbound.PriceRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component // Esta anotación sí es de Spring, ya que estamos en la capa de infraestructura
public class PriceDatabaseAdapter implements PriceRepositoryPort {

    private final SpringDataPriceRepository repository;

    public PriceDatabaseAdapter(SpringDataPriceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return repository.findApplicablePrice(brandId, productId, applicationDate)
                .map(this::mapToDomain);
    }

    // Mapeador manual: Limpio, rápido, eficiente y libre de dependencias de terceros
    private Price mapToDomain(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }
}