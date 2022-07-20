package kr.toyauction.infra.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "property.intra")
public class IntraProperty {
	private String awsS3Host;
	private String awsS3Bucket;
	private String awsS3AccessKeyId;
	private String awsS3SecretAccessKey;

}
