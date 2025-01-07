package az.company.xecurrencyconverter.util;

import lombok.Getter;

@Getter
public enum ExchangeStatus {
    SUCCESS("SUCCESS", "OK"),
    FAILURE("FAILURE", "Əməliyyat uğursuz oldu."),
    INVALID_REQUEST_BODY("INVALID_REQUEST_BODY", "Zəhmət olmasa keçərli məlumat göndərin.");

    private final String code;
    private final String message;

    ExchangeStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
