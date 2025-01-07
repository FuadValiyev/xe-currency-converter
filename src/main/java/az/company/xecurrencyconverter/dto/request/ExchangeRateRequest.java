package az.company.xecurrencyconverter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequest {

    private String date;
    private String currencyCode;
    private Double amountAZN;

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = (currencyCode == null) ? null : currencyCode.toUpperCase();
    }
}
