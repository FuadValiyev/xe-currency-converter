package az.company.xecurrencyconverter.dto.bean;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConverterBean {

    private String date;
    private String fromCurrency;
    private String toCurrency;
    private String convertedAmount;
}
