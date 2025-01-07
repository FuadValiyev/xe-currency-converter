package az.company.xecurrencyconverter.controller;

import az.company.xecurrencyconverter.dto.request.CurrencyRequest;
import az.company.xecurrencyconverter.dto.response.CurrencyResponse;
import az.company.xecurrencyconverter.service.CentralBankApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class CurrencyController {

    private final CentralBankApiService centralBankApiService;

    @PostMapping("/getCurrency")
    public ResponseEntity<CurrencyResponse> getCurrency(@RequestBody CurrencyRequest request) {
        CurrencyResponse response = centralBankApiService.getCurrencyACB(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCurrency")
    public ResponseEntity<CurrencyResponse> getCurrency(
            @RequestParam String date,
            @RequestParam String currencyCode
    ) {
        CurrencyRequest request = new CurrencyRequest(date, currencyCode);
        CurrencyResponse response = centralBankApiService.getCurrencyACB(request);
        return ResponseEntity.ok(response);
    }
}
