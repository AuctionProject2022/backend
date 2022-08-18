package kr.toyauction.global.exception;


import kr.toyauction.global.error.GlobalErrorCode;

/**
 * validation 검사 실패
 */
public class WrongValueException extends BusinessException {
    public WrongValueException(String message) {
        super(GlobalErrorCode.G0003,message);
    }
}
