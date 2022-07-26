package kr.toyauction.global.dto;

import kr.toyauction.global.error.ErrorCode;
import kr.toyauction.global.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ErrorResponseHelper {

    private final MessageSource messageSource;

    public ResponseEntity<ErrorResponse> code(ErrorCode errorCode, String... args) {
        String errorMessage = messageSource.getMessage(errorCode.name(), args, LocaleContextHolder.getLocale());
        return ResponseEntity
                .status(errorCode.status())
                .body(new ErrorResponse(errorCode.name(), errorMessage));
    }

    public ResponseEntity<ErrorResponse> overlapError(ErrorCode errorCode, FieldError fieldError) {
        String errorMessage = messageSource.getMessage(errorCode.name(), null, LocaleContextHolder.getLocale());

        FieldErrorResponse response = new FieldErrorResponse(fieldError,fieldError.getDefaultMessage());
        response.setCode("overlap");
        List<FieldErrorResponse> responseList = new ArrayList<>();
        responseList.add(response);

        return ResponseEntity
                .status(errorCode.status())
                .body(new ErrorResponse(errorCode.name(), errorMessage,responseList));
    }

    public ResponseEntity<ErrorResponse> bindErrors(Errors errors) {
        List<FieldErrorResponse> details = errors
                .getFieldErrors()
                .stream()
                .map(error -> {
                    String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());
                    return new FieldErrorResponse(error, errorMessage);
                }).collect(Collectors.toList());

        ErrorCode errorCode = GlobalErrorCode.G0001;
        String errorMessage = messageSource.getMessage(errorCode.name(), null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(errorCode.status())
                .body(new ErrorResponse(errorCode.name(), errorMessage, details));
    }
}
