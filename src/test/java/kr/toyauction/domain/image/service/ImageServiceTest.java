package kr.toyauction.domain.image.service;

import kr.toyauction.domain.image.dto.ImagePostRequest;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.repository.ImageRepository;
import kr.toyauction.global.property.TestProperty;
import kr.toyauction.infra.aws.client.IntraAwsS3Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@ExtendWith({MockitoExtension.class})
class ImageServiceTest {

	@Autowired
	ImageRepository imageRepository;

	@Mock
	IntraAwsS3Client intraAwsS3Client;

	ImageService imageService;

	ResourceLoader resourceLoader;

	@BeforeEach
	void setMemberService() {
		imageService = new ImageService(imageRepository, intraAwsS3Client);
		resourceLoader = new GenericWebApplicationContext();
	}

	@Test
	@DisplayName("success : 파일 생성")
	void save() throws IOException {

		// given
		MockMultipartFile file = new MockMultipartFile(
				"image",
				TestProperty.PNG_FILENAME,
				MediaType.IMAGE_PNG_VALUE,
				resourceLoader.getResource(TestProperty.PNG_CLASSPATH).getInputStream());
		ImagePostRequest request = ImagePostRequest.builder()
				.image(file)
				.build();

		// when
		ImageEntity saved = imageService.save(request);

		// then
		assertNotNull(saved.getId());
		assertNotNull(saved.getMemberId());
		assertNull(saved.getType());
		assertNull(saved.getTargetId());
		assertNotNull(saved.getPath());
	}

	@Test
	@DisplayName("fail : file 이 null 인 경우")
	void saveFileIsNull() {

		// given
		MockMultipartFile file = null;
		ImagePostRequest request = ImagePostRequest.builder()
				.image(file)
				.build();

		// when
		assertThrows(NullPointerException.class, () -> {
			imageService.save(request);
		});

		// then
	}


	@Test
	@DisplayName("success : 사용자는 상품등록시 썸네일 및 상품이미지 번호를 입력할 경우 성공적으로 저장 할 수 있습니다.")
	void updateProductImageTarget() {


		// given

		ImageEntity thumbnailImage = ImageEntity.builder()
				.memberId(0L)
				.path("test.png")
				.build();
		ImageEntity image1 = ImageEntity.builder()
				.memberId(0L)
				.path("test.png")
				.build();
		ImageEntity image2 = ImageEntity.builder()
				.memberId(0L)
				.path("test.png")
				.build();

		ImageEntity savedThumbnailImage = imageRepository.save(thumbnailImage);
		ImageEntity savedImage1 = imageRepository.save(image1);
		ImageEntity savedImage2 = imageRepository.save(image2);

		Long targetId = 90L; // TODO get Product


		// when
		imageService.updateProductTarget(savedThumbnailImage.getId(), List.of(savedImage1.getId(), savedImage2.getId()), targetId);

		// then
		ImageEntity updateThumbnailImage = imageRepository.findById(savedThumbnailImage.getId()).get();
		ImageEntity updateImage1 = imageRepository.findById(savedThumbnailImage.getId()).get();
		ImageEntity updateImage2 = imageRepository.findById(savedThumbnailImage.getId()).get();


		assertNotNull(updateThumbnailImage.getTargetId());
		assertNotNull(updateImage1.getTargetId());
		assertNotNull(updateImage2.getTargetId());
		assertNotNull(updateThumbnailImage.getType());
		assertNotNull(updateImage1.getType());
		assertNotNull(updateImage2.getType());

	}
}