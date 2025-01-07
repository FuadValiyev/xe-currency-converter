package az.company.xecurrencyconverter.controller;

import az.company.xecurrencyconverter.dto.request.ConverterRequest;
import az.company.xecurrencyconverter.dto.response.ConverterResponse;
import az.company.xecurrencyconverter.service.ConverterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/converter")
@RequiredArgsConstructor
public class ConverterController {

    private final ConverterService converterService;

    @PostMapping("/convert")
    public ResponseEntity<ConverterResponse> convertCurrency(@Valid @RequestBody ConverterRequest request) {
        return ResponseEntity.ok(converterService.convert(request));

    }
}