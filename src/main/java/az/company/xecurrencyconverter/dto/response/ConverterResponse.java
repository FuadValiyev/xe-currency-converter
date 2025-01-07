package az.company.xecurrencyconverter.dto.response;

import az.company.xecurrencyconverter.dto.bean.ConverterBean;
import az.company.xecurrencyconverter.dto.bean.ResponseBean;
import az.company.xecurrencyconverter.util.ExchangeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConverterResponse extends ResponseBean<ConverterBean> {

    public ConverterResponse(ExchangeStatus exchangeStatus, String message) {
        super(exchangeStatus, message);
    }
}
