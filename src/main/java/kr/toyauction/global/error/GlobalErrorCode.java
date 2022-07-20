package kr.toyauction.global.error;

import org.springframework.http.HttpStatus;

/**
 * GlobalErrorCode
 * 전역에서 공통으로 발생되는 오류코드
 */
public enum GlobalErrorCode implements ErrorCode {

    G0000(HttpStatus.INTERNAL_SERVER_ERROR)
    ,G0001(HttpStatus.BAD_REQUEST)
    ,G0002(HttpStatus.NOT_FOUND)
    ,G0003(HttpStatus.BAD_REQUEST)
    ,G0004(HttpStatus.BAD_REQUEST)
    ,G0005(HttpStatus.BAD_REQUEST)
    ,G0006(HttpStatus.BAD_REQUEST)
    ;

    private final HttpStatus status;

    GlobalErrorCode(HttpStatus status) {
        this.status = status;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
