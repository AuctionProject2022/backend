package kr.toyauction.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus status();
    String name();
}
