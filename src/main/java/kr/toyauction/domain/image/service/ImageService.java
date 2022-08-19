package kr.toyauction.domain.image.service;

import io.jsonwebtoken.lang.Collections;
import kr.toyauction.domain.image.dto.ImagePostRequest;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.entity.ImageType;
import kr.toyauction.domain.image.repository.ImageRepository;
import kr.toyauction.global.exception.DomainNotFoundException;
import kr.toyauction.global.util.CommonUtils;
import kr.toyauction.infra.aws.client.IntraAwsS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	private final IntraAwsS3Client intraAwsS3Client;

	@Transactional
	public ImageEntity save(final ImagePostRequest request, final Long memberId) {

		String prefixKey = CommonUtils.generateS3PrefixKey();
		String randomFilename = CommonUtils.generateRandomFilename(Objects.requireNonNull(request.getImage().getOriginalFilename()));

		ImageEntity imageEntity = ImageEntity.builder()
				.memberId(memberId) // TODO
				.path(prefixKey + randomFilename)
				.build();
		imageEntity.validation();
		intraAwsS3Client.upload(request.getImage(), prefixKey + randomFilename);
		return imageRepository.save(imageEntity);
	}

	@Transactional
	public void updateProductTarget(@NonNull final Long thumbnailImageId,
									@NonNull final Collection<Long> imageIds,
									@NonNull final Long targetId) {

		ImageEntity thumbnail = imageRepository.findById(thumbnailImageId)
				.orElseThrow(() -> new DomainNotFoundException(thumbnailImageId));
		thumbnail.updateType(ImageType.PRODUCT_THUMBNAIL, targetId);

		if (!Collections.isEmpty(imageIds)) {
			List<ImageEntity> imageEntities = imageRepository.findAllById(imageIds);
			for (ImageEntity imageEntity : imageEntities) {
				imageEntity.updateType(ImageType.PRODUCT, targetId);
			}
		}
	}
}
