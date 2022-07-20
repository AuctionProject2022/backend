package kr.toyauction.global.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {

    private boolean success = false;
    private String code;
    private String message;
    private List<FieldErrorResponse> errors;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(String code, String message, List<FieldErrorResponse> fieldErrors) {
        this.code = code;
        this.message = message;
        this.errors = fieldErrors;
    }
}
