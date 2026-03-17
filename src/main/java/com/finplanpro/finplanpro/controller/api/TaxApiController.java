package com.finplanpro.finplanpro.controller.api;

import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.dto.TaxResultDTO;
import com.finplanpro.finplanpro.service.TaxCalculationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tax")
public class TaxApiController {

    private final TaxCalculationService taxCalculationService;

    public TaxApiController(TaxCalculationService taxCalculationService) {
        this.taxCalculationService = taxCalculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<TaxResultDTO> calculateTax(@Valid @RequestBody TaxRequestDTO requestDTO) {
        TaxResultDTO result = taxCalculationService.calculateTax(requestDTO);
        return ResponseEntity.ok(result);
    }
}
