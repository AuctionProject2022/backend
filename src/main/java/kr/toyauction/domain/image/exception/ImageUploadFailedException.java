package kr.toyauction.domain.image.exception;

import kr.toyauction.domain.image.error.ImageErrorCode;
import kr.toyauction.global.exception.BusinessException;

public class ImageUploadFailedException extends BusinessException {

	public ImageUploadFailedException() {
		super(ImageErrorCode.F0000);
	}
}
