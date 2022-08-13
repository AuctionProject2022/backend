package kr.toyauction.domain.image.service;

import kr.toyauction.domain.image.dto.ImagePostRequest;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.entity.ImageType;
import kr.toyauction.domain.image.repository.ImageRepository;
import kr.toyauction.global.util.CommonUtils;
import kr.toyauction.infra.aws.client.IntraAwsS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	public ImageEntity save(final ImagePostRequest request) {

		String prefixKey = CommonUtils.generateS3PrefixKey();
		String randomFilename = CommonUtils.generateRandomFilename(Objects.requireNonNull(request.getImage().getOriginalFilename()));

		ImageEntity imageEntity = ImageEntity.builder()
				.memberId(0L) // TODO
				.path(prefixKey + randomFilename)
				.build();
		imageEntity.validation();
		intraAwsS3Client.upload(request.getImage(), prefixKey + randomFilename);
		return imageRepository.save(imageEntity);
	}

	@Transactional
	public void registerTargetId(final Collection<Long> imageId,
								 final ImageType imageType,
								 final Long targetId) {

		List<ImageEntity> imageEntities = imageRepository.findAllById(imageId);

		for (ImageEntity imageEntity : imageEntities) {
			imageEntity.updateType(imageType, targetId);
		}

		imageRepository.saveAll(imageEntities);
	}
}
