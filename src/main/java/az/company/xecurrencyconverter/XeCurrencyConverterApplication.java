package az.company.xecurrencyconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class XeCurrencyConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(XeCurrencyConverterApplication.class, args);
    }

}
