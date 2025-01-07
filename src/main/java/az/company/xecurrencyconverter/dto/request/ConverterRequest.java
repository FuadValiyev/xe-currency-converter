package az.company.xecurrencyconverter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConverterRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Source currency is required")
    @Size(min = 3, max = 3, message = "Source currency code must be 3 characters")
    private String from;

    @NotBlank(message = "Target currency is required")
    @Size(min = 3, max = 3, message = "Target currency code must be 3 characters")
    private String to;
}