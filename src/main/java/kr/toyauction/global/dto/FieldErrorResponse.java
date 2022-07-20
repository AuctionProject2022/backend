package kr.toyauction.global.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Getter
@Setter
public class FieldErrorResponse {

    String field;
    String code;
    String message;

    public FieldErrorResponse(FieldError fieldError, String message) {
        this.field = fieldError.getField();
        this.code = fieldError.getCode();
        this.message = message;
    }
}
