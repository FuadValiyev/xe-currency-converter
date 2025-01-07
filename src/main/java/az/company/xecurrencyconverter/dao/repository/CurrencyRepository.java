package az.company.xecurrencyconverter.dao.repository;

import az.company.xecurrencyconverter.dao.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByDateAndCurrencyCode(LocalDate date, String currencyCode);
}
