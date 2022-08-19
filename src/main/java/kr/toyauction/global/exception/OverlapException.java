package kr.toyauction.global.exception;


import kr.toyauction.global.error.GlobalErrorCode;

/**
 * 중복 값
 */
public class OverlapException extends BusinessException {
    public OverlapException() {
        super(GlobalErrorCode.G0009);
    }
}
