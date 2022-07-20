package kr.toyauction.domain.image.controller;

import kr.toyauction.domain.image.dto.ImagePostRequest;
import kr.toyauction.domain.image.dto.ImagePostResponse;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.service.ImageService;
import kr.toyauction.domain.image.validation.ImageValidator;
import kr.toyauction.global.dto.SuccessResponse;
import kr.toyauction.global.dto.SuccessResponseHelper;
import kr.toyauction.global.property.Url;
import kr.toyauction.infra.property.IntraProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;
	private final ImageValidator imageValidator;
	private final IntraProperty intraProperty;

	@InitBinder
	protected void initBinders(WebDataBinder binder) {
		binder.addValidators(imageValidator);
	}


	@PostMapping(value = Url.IMAGE)
	public SuccessResponse<ImagePostResponse> postFile(@Validated ImagePostRequest request) {
		ImageEntity imageEntity = imageService.save(request);
		return SuccessResponseHelper.success(new ImagePostResponse(imageEntity, intraProperty.getAwsS3Host()));
	}
}
