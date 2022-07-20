package kr.toyauction.domain.image.error;

import kr.toyauction.global.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ImageErrorCode implements ErrorCode {

	F0000(HttpStatus.INTERNAL_SERVER_ERROR),
	F0001(HttpStatus.BAD_REQUEST);

	private final HttpStatus status;

	ImageErrorCode(HttpStatus status) {
		this.status = status;
	}

	@Override
	public HttpStatus status() {
		return status;
	}
}
