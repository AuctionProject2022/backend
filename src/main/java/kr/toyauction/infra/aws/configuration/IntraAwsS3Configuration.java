package kr.toyauction.infra.aws.configuration;

import kr.toyauction.infra.property.IntraProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class IntraAwsS3Configuration {

	private final IntraProperty applicationProperty;

	@Bean
	public S3Client s3Client() {
		AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
				applicationProperty.getAwsS3AccessKeyId(),
				applicationProperty.getAwsS3SecretAccessKey());
		return S3Client.builder()
				.credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
				.region(Region.AP_NORTHEAST_2)
				.build();
	}
}
