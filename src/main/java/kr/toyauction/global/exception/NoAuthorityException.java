package kr.toyauction.global.exception;

import kr.toyauction.global.error.GlobalErrorCode;

public class NoAuthorityException extends BusinessException {
    public NoAuthorityException() {super(GlobalErrorCode.G0009);}
}