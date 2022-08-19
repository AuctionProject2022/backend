package kr.toyauction.global.handler;

import kr.toyauction.global.dto.ErrorResponse;
import kr.toyauction.global.dto.ErrorResponseHelper;
import kr.toyauction.global.error.GlobalErrorCode;
import kr.toyauction.global.exception.BusinessException;
import kr.toyauction.global.exception.DomainNotFoundException;
import kr.toyauction.global.exception.OverlapException;
import kr.toyauction.global.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorResponseHelper errorResponseHelper;

    @ExceptionHandler(OverlapException.class)
    public ResponseEntity<ErrorResponse> handleOverlapException(OverlapException e) {
        log.error("OverlapException : ", e);
        return errorResponseHelper.code(GlobalErrorCode.G0009);
    }

    @ExceptionHandler(WrongValueException.class)
    public ResponseEntity<ErrorResponse> handleWrongValueException(WrongValueException e) {
        log.error("WrongValueException : ", e);
        return errorResponseHelper.code(GlobalErrorCode.G0003,e.getErrorMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e, AuthenticationException authException) {
        log.error("AccessDeniedException : ", e);
        return errorResponseHelper.code(GlobalErrorCode.G0008);
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDomainNotFoundException(DomainNotFoundException e) {
        log.error("DomainNotFoundException : ", e);
        if (e.getId() != null) {
            log.error("ID not found : {}", e.getId());
        }
        return errorResponseHelper.code(e.getErrorCode());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return errorResponseHelper.code(GlobalErrorCode.G0005);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return errorResponseHelper.code(GlobalErrorCode.G0006);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException : ", e);
        return errorResponseHelper.code(e.getErrorCode());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException :", e);
        return errorResponseHelper.code(GlobalErrorCode.G0005);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        return errorResponseHelper.bindErrors(e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return errorResponseHelper.code(GlobalErrorCode.G0004);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException() {
        return errorResponseHelper.code(GlobalErrorCode.G0002); // 404 의 경우 log 생략
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException : ", e);
        return errorResponseHelper.code(GlobalErrorCode.G0000);
    }
}
