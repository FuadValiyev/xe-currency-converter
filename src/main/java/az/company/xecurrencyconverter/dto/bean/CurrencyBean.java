package az.company.xecurrencyconverter.dto.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyBean {

    private String date;
    private String name;
    private String currencyCode;
    private double nominal;
    private double value;
}
