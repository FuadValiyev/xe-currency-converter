package az.company.xecurrencyconverter.service;

import az.company.xecurrencyconverter.dao.entity.CurrencyEntity;
import az.company.xecurrencyconverter.dao.repository.CurrencyRepository;
import az.company.xecurrencyconverter.dto.bean.CurrencyBean;
import az.company.xecurrencyconverter.dto.request.CurrencyRequest;
import az.company.xecurrencyconverter.dto.response.CurrencyResponse;
import az.company.xecurrencyconverter.util.ExchangeStatus;
import az.company.xecurrencyconverter.util.XmlUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static az.company.xecurrencyconverter.util.Utility.isNullOrEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class CentralBankApiService {

    private final CurrencyRepository exchangeRateRepository;

    @Value("${centralBankApi.base_url}")
    private String BASE_URL;

    @Cacheable(cacheNames = "exchangeRateCache", key = "#request.date + '_' + #request.currencyCode")
    public CurrencyResponse getCurrencyACB(CurrencyRequest request) {
        CurrencyResponse response = new CurrencyResponse();

        CurrencyBean bean = new CurrencyBean();
        bean.setDate(request.getDate());
        bean.setCurrencyCode(request.getCurrencyCode());

        if (isNullOrEmpty(request.getDate()) || isNullOrEmpty(request.getCurrencyCode())) {
            return new CurrencyResponse(ExchangeStatus.INVALID_REQUEST_BODY, null);
        }

        LocalDate parsedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            parsedDate = LocalDate.parse(request.getDate(), formatter);
        } catch (DateTimeParseException e) {
            return new CurrencyResponse(ExchangeStatus.FAILURE,
                    "Tarix formatı düzgün deyil: " + request.getDate());
        }

        Optional<CurrencyEntity> optionalExchangeRate =
                exchangeRateRepository.findByDateAndCurrencyCode(parsedDate, request.getCurrencyCode());

        if (optionalExchangeRate.isPresent()) {
            CurrencyEntity cachedRate = optionalExchangeRate.get();
            response.setStatus(ExchangeStatus.SUCCESS.getCode());
            response.setMessage(ExchangeStatus.SUCCESS.getMessage());
            bean.setValue(cachedRate.getValue());
            bean.setName(cachedRate.getName());
            bean.setNominal(cachedRate.getNominal());
            response.setData(bean);
            return response;
        }

        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                URL url = new URL(BASE_URL + request.getDate() + ".xml");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int statusCode = conn.getResponseCode();

                if (statusCode >= 500 && statusCode < 600) {
                    throw new RuntimeException("Mərkəzi Bankın serveri cavab vermir. Status kodu: \" + statusCode");
                }

                Document doc = XmlUtility.parseXml(conn.getInputStream());
                doc.getDocumentElement().normalize();

                NodeList valuteList = doc.getElementsByTagName("Valute");
                boolean found = false;
                double value = 0, nominal = 0;
                String nameOfCurrencyCode = "";

                for (int i = 0; i < valuteList.getLength(); i++) {
                    Element element = (Element) valuteList.item(i);
                    if (request.getCurrencyCode().equals(element.getAttribute("Code"))) {
                        value = Double.parseDouble(
                                element.getElementsByTagName("Value").item(0).getTextContent()
                        );
                        nominal = Double.parseDouble(element.getElementsByTagName("Nominal").item(0)
                                .getTextContent()
                                .replaceAll("\\D+", ""));

                        nameOfCurrencyCode = element.getElementsByTagName("Name")
                                .item(0)
                                .getTextContent()
                                .replaceFirst("^\\d+\\s+", "");

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    throw new RuntimeException("Valyuta tapılmadı: " + request.getCurrencyCode());
                } else {
                    response.setStatus(ExchangeStatus.SUCCESS.getCode());
                    response.setMessage(ExchangeStatus.SUCCESS.getMessage());
                    bean.setName(nameOfCurrencyCode);
                    bean.setValue(value);
                    bean.setNominal(nominal);
                    response.setData(bean);

                    CurrencyEntity entity = CurrencyEntity.builder()
                            .date(parsedDate)
                            .currencyCode(request.getCurrencyCode())
                            .value(value)
                            .nominal(nominal)
                            .name(nameOfCurrencyCode)
                            .build();
                    exchangeRateRepository.save(entity);
                }
                return response;

            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    log.error("Mərkəzi Bankdan məlumat alınarkən xəta baş verdi: ", e);
                    throw new RuntimeException("Mərkəzi Bankdan məlumat alınarkən xəta baş verdi.", e);
                }
                log.error("An error occurred on attempt {}:", e.getMessage());
            }
        }
        return new CurrencyResponse(ExchangeStatus.FAILURE, null);
    }
}
