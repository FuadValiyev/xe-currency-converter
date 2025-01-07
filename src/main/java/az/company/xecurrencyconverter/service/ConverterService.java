package az.company.xecurrencyconverter.service;

import az.company.xecurrencyconverter.dto.bean.ConverterBean;
import az.company.xecurrencyconverter.dto.bean.CurrencyBean;
import az.company.xecurrencyconverter.dto.request.ConverterRequest;
import az.company.xecurrencyconverter.dto.request.CurrencyRequest;
import az.company.xecurrencyconverter.dto.response.ConverterResponse;
import az.company.xecurrencyconverter.dto.response.CurrencyResponse;
import az.company.xecurrencyconverter.util.ExchangeStatus;
import az.company.xecurrencyconverter.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static az.company.xecurrencyconverter.util.Utility.getTodayDateForBankApi;
import static az.company.xecurrencyconverter.util.Utility.isNullOrEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConverterService {

    private final CentralBankApiService centralBankApi;

    public ConverterResponse convert(ConverterRequest request) {
        log.info("Starting conversion: {} {} to {}", request.getAmount(), request.getFrom(), request.getTo());

        if (isNullOrEmpty(request.getAmount()) || isNullOrEmpty(request.getFrom()) || isNullOrEmpty(request.getTo())) {
            return new ConverterResponse(ExchangeStatus.INVALID_REQUEST_BODY, null);
        }

        if (request.getTo().equalsIgnoreCase("azn")) {

            CurrencyRequest currencyRequest = new CurrencyRequest();
            currencyRequest.setCurrencyCode(request.getFrom());
            currencyRequest.setDate(getTodayDateForBankApi());
            CurrencyResponse currencyResponse = centralBankApi.getCurrencyACB(currencyRequest);

            ConverterResponse response = new ConverterResponse();
            response.setStatus(ExchangeStatus.SUCCESS.getCode());
            response.setMessage(ExchangeStatus.SUCCESS.getMessage());
            response.setData(ConverterBean.builder()
                    .convertedAmount(Utility.toString(currencyResponse.getData().getValue() * request.getAmount()))
                    .date(currencyResponse.getData().getDate())
                    .fromCurrency(request.getFrom().toUpperCase())
                    .toCurrency(request.getTo().toUpperCase())
                    .build());
            return response;
        }

        if (request.getFrom().equalsIgnoreCase("azn")){

            CurrencyRequest currencyRequest = new CurrencyRequest();
            currencyRequest.setCurrencyCode(request.getTo());
            currencyRequest.setDate(getTodayDateForBankApi());
            CurrencyResponse currencyResponse = centralBankApi.getCurrencyACB(currencyRequest);

            ConverterResponse response = new ConverterResponse();
            response.setStatus(ExchangeStatus.SUCCESS.getCode());
            response.setMessage(ExchangeStatus.SUCCESS.getMessage());
            response.setData(ConverterBean.builder()
                    .convertedAmount(Utility.toString(
                            1/(currencyResponse.getData().getValue()) * request.getAmount()))
                    .date(currencyResponse.getData().getDate())
                    .fromCurrency(request.getFrom().toUpperCase())
                    .toCurrency(request.getTo().toUpperCase())
                    .build());
            return response;
        }

        try {
            CurrencyRequest fromCurrencyRequest = new CurrencyRequest();
            fromCurrencyRequest.setCurrencyCode(request.getFrom());
            fromCurrencyRequest.setDate(getTodayDateForBankApi());
            CurrencyResponse fromCurrencyResponse = centralBankApi.getCurrencyACB(fromCurrencyRequest);

            if (isNullOrEmpty(fromCurrencyResponse.getData())) {
                log.error("Failed to fetch data for currency: {}", request.getFrom());
                return new ConverterResponse(ExchangeStatus.FAILURE,
                        "Failed to fetch data for currency: " + request.getFrom());
            }

            CurrencyBean fromCurrency = fromCurrencyResponse.getData();

            CurrencyRequest toCurrencyRequest = new CurrencyRequest();
            toCurrencyRequest.setCurrencyCode(request.getTo());
            toCurrencyRequest.setDate(getTodayDateForBankApi());
            CurrencyResponse toCurrencyResponse = centralBankApi.getCurrencyACB(toCurrencyRequest);

            if (isNullOrEmpty(toCurrencyResponse.getData())) {
                log.error("Failed to fetch data for currency: {}", request.getTo());
                return new ConverterResponse(ExchangeStatus.FAILURE,
                        "Failed to fetch data for currency: " + request.getTo());
            }

            CurrencyBean toCurrency = toCurrencyResponse.getData();

            double exchangeRate = calculateExchangeRate(fromCurrency, toCurrency);
            log.info("Exchange rate from {} to {}: {}", request.getFrom(), request.getTo(), exchangeRate);

            double convertedAmount = request.getAmount() * exchangeRate;
            log.info("Converted amount: {}", convertedAmount);

            ConverterResponse response = new ConverterResponse();
            response.setStatus(ExchangeStatus.SUCCESS.getCode());
            response.setMessage(ExchangeStatus.SUCCESS.getMessage());
            response.setData(ConverterBean.builder()
                    .convertedAmount(Utility.toString(convertedAmount))
                    .date(fromCurrency.getDate())
                    .fromCurrency(request.getFrom().toUpperCase())
                    .toCurrency(request.getTo().toUpperCase())
                    .build());
            return response;

        } catch (Exception e) {
            log.error("Error during currency conversion", e);
            return new ConverterResponse(ExchangeStatus.FAILURE,
                    "An error occurred during conversion");
        }
    }

    private double calculateExchangeRate(CurrencyBean from, CurrencyBean to) {
        double fromRate = from.getValue() / from.getNominal();
        double toRate = to.getValue() / to.getNominal();

        return fromRate / toRate;
    }
}
