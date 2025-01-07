package az.company.xecurrencyconverter.dto.response;

import az.company.xecurrencyconverter.dto.bean.ExchangeRateBean;
import az.company.xecurrencyconverter.dto.bean.ResponseBean;
import az.company.xecurrencyconverter.util.ExchangeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateResponse extends ResponseBean<ExchangeRateBean> {

    public ExchangeRateResponse(ExchangeStatus exchangeStatus, String message) {
        super(exchangeStatus, message);
    }
}
