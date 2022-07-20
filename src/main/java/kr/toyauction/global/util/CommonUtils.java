package kr.toyauction.global.util;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommonUtils {

	/**
	 * s3 에 업로드 하기 위한 prefix key 생성
	 *
	 * @return public/yyyy/M-d/
	 */
	public static String generateS3PrefixKey() {
		LocalDateTime now = LocalDateTime.now();
		return "images/" +
				now.getYear() +
				"-" +
				now.getMonthValue() +
				"-" +
				now.getDayOfMonth() +
				"/";
	}

	/**
	 * MultipartFile 으로부터 확장자를 가져와서 랜덤 파일명을 생성
	 *
	 * @param fileName 원본 파일명
	 * @return 랜덤으로 변경된 파일명
	 */
	public static String generateRandomFilename(@NonNull final String fileName) {
		UUID uuid = UUID.randomUUID();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		return uuid + "." + ext;
	}
}
