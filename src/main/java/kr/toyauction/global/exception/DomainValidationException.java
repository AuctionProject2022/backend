package kr.toyauction.global.exception;


import kr.toyauction.global.error.GlobalErrorCode;

/**
 * 도메인 생성시 validation 검사 실패
 */
public class DomainValidationException extends BusinessException {
    public DomainValidationException() {
        super(GlobalErrorCode.G0000);
    }
}
