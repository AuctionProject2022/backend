package kr.toyauction.global.exception;

import kr.toyauction.global.error.ErrorCode;
import lombok.Getter;

/**
 * BusinessException
 * 프로젝트 내에서 정의한 오류 발생시 상속받는 최상위 객체
 */
@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String[] args;
	private final String errorMessage;

	public BusinessException(final ErrorCode errorCode, final String... args) {
		this.errorCode = errorCode;
		this.args = args;
		this.errorMessage = null;
	}

	public BusinessException(final ErrorCode errorCode, final String errorMessage, final String... args) {
		this.errorCode = errorCode;
		this.args = args;
		this.errorMessage = errorMessage;
	}
}
