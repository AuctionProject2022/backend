package kr.toyauction.global.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.toyauction.global.dto.ErrorResponse;
import kr.toyauction.global.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("UnAuthorizaed!!! message : " + authException.getMessage());
        String exception = (String)request.getAttribute(JwtEnum.EXCEPTION_PRODUCE.getDescription());    //만료된 토큰을 구분하려고 만듬

        // 바디에 내가 원하는 형태의 값의 지정과 만료된 토큰이 아닐 시 잘못된 토큰으로 코드와 메세지 전달
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        GlobalErrorCode errorCode = GlobalErrorCode.G0007;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.name(),messageSource.getMessage(errorCode.name(), null, LocaleContextHolder.getLocale()));

        // 토큰만료 시 코드와 메시지 변경
        if (exception != null && exception.equals(JwtEnum.ERROR_EXPIRED_TOKEN.getDescription())){
            errorCode = GlobalErrorCode.G0008;
            errorResponse.setCode(errorCode.name());
            errorResponse.setMessage(messageSource.getMessage(errorCode.name(), null, LocaleContextHolder.getLocale()));
        }

        // 바디 전달
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, errorResponse);
            os.flush();
        }
    }
}