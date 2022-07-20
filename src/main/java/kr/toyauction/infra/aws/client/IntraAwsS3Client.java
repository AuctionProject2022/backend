package kr.toyauction.infra.aws.client;

import kr.toyauction.global.error.GlobalErrorCode;
import kr.toyauction.global.exception.BusinessException;
import kr.toyauction.infra.property.IntraProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class IntraAwsS3Client {

	private final S3Client s3Client;
	private final IntraProperty intraProperty;

	public PutObjectResponse upload(@NonNull final MultipartFile multipartFile,
									@NonNull final String key) {

		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(intraProperty.getAwsS3Bucket())
				.key(key)
				.build();

		try {
			RequestBody requestBody = RequestBody.fromInputStream(
					multipartFile.getInputStream(), multipartFile.getSize());
			return s3Client.putObject(putObjectRequest, requestBody);
		} catch (IOException e) {
			log.error("IntraAwsS3Client pubObject is failed");
			throw new BusinessException(GlobalErrorCode.G0000); // TODO
		}
	}
}
