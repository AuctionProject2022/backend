package kr.toyauction.domain.image.validation;

import kr.toyauction.domain.image.dto.ImagePostRequest;
import kr.toyauction.domain.image.error.ImageErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ImagePostRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target instanceof ImagePostRequest) {
			ImagePostRequest request = (ImagePostRequest) target;
			if (request.getImage() != null) {
				if (!enableContentTypes(request.getImage().getContentType())) {
					errors.rejectValue("image", ImageErrorCode.F0001.name(), new String[]{"jpg, png"}, null);
				}
			}
		}
	}

	private boolean enableContentTypes(final String contentType) {
		String[] enableContentTypes = {
				"image/jpeg",
				"image/png"
		};
		for (String enableContentType : enableContentTypes) {
			if (enableContentType.equals(contentType)) {
				return true;
			}
		}
		return false;
	}
}
