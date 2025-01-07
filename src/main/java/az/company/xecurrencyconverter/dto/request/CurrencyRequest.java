package az.company.xecurrencyconverter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRequest {

    private String date;
    private String currencyCode;

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = (currencyCode == null) ? null : currencyCode.toUpperCase();
    }
}