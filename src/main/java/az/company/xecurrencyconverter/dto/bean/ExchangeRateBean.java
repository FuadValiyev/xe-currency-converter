package az.company.xecurrencyconverter.dto.bean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateBean {

    private String date;
    private String name;
    private String currencyCode;
    private double amountAZN;
    private double value;
}
