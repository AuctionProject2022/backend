package kr.toyauction.domain.image.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class ImagePostRequest {

	@NotNull
	private MultipartFile image;
}
