package az.company.xecurrencyconverter.exception;

import az.company.xecurrencyconverter.dto.response.ConverterResponse;
import az.company.xecurrencyconverter.util.ExchangeStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ConverterResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        ConverterResponse response = new ConverterResponse(ExchangeStatus.FAILURE, errors.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
