package az.company.xecurrencyconverter.dto.bean;

import az.company.xecurrencyconverter.util.ExchangeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static az.company.xecurrencyconverter.util.Utility.isNullOrEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean<T> {

    T data;
    private String status;
    private String message;

    public ResponseBean(ExchangeStatus data, String message) {
        this.status = data.getCode();
        if (isNullOrEmpty(message)) {
            this.message = data.getMessage();
        } else {
            this.message = message;
        }
    }
}
