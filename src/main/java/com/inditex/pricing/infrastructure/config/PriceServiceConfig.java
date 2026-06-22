package com.inditex.pricing.infrastructure.config;

// 1. Cambiamos el import antiguo por la ruta del nuevo servicio
import com.inditex.pricing.domain.service.PriceService;
import com.inditex.pricing.ports.inbound.GetApplicablePriceUseCase;
import com.inditex.pricing.ports.outbound.PriceRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceServiceConfig {

    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(PriceRepositoryPort priceRepositoryPort) {
        // 2. Instanciamos 'PriceService' en lugar de la clase vieja
        return new PriceService(priceRepositoryPort);
    }
}