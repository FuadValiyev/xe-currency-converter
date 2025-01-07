package az.company.xecurrencyconverter.dto.response;

import az.company.xecurrencyconverter.dto.bean.CurrencyBean;
import az.company.xecurrencyconverter.dto.bean.ResponseBean;
import az.company.xecurrencyconverter.util.ExchangeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyResponse extends ResponseBean<CurrencyBean> {

    public CurrencyResponse(ExchangeStatus exchangeStatus, String message) {
        super(exchangeStatus, message);
    }
}
