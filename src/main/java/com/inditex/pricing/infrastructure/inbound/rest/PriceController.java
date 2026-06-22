package com.inditex.pricing.infrastructure.inbound.rest;

import com.inditex.pricing.domain.model.Price;
import com.inditex.pricing.ports.inbound.GetApplicablePriceUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    public PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase) {
        this.getApplicablePriceUseCase = getApplicablePriceUseCase;
    }

    @GetMapping
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam("brandId") Long brandId,
            @RequestParam("productId") Long productId,
            @RequestParam("applicationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {

        Price price = getApplicablePriceUseCase.execute(brandId, productId, applicationDate);
        return ResponseEntity.ok(mapToResponse(price));
    }

    private PriceResponse mapToResponse(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price(),
                price.currency()
        );
    }
}