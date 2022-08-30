package kr.toyauction.global.exception;


import kr.toyauction.global.error.GlobalErrorCode;
import lombok.Getter;
import org.springframework.validation.FieldError;

/**
 * 중복 값
 */
@Getter
public class OverlapException extends BusinessException {
    FieldError error;

    public OverlapException(String fieId) {
        super(GlobalErrorCode.G0001);
        this.error = new FieldError("overlap",fieId,"중복 닉네임 입니다.");
    }
}
