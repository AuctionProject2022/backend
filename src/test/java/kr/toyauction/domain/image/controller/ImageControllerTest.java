package kr.toyauction.domain.image.controller;

import kr.toyauction.domain.image.dto.ImagePostRequest;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.service.ImageService;
import kr.toyauction.global.error.GlobalErrorCode;
import kr.toyauction.global.property.TestProperty;
import kr.toyauction.global.property.Url;
import kr.toyauction.global.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class ImageControllerTest {

	MockMvc mockMvc;

	@Autowired
	ResourceLoader resourceLoader;

	@MockBean
	ImageService imageService;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(
						documentationConfiguration(restDocumentation)
								.uris()
								.withScheme(TestProperty.SPRING_REST_DOCS_SERVER_SCHEME)
								.withHost(TestProperty.SPRING_REST_DOCS_SERVER_HOST)
								.withPort(TestProperty.SPRING_REST_DOCS_SERVER_PORT)
								.and()
								.operationPreprocessors()
								.withRequestDefaults(prettyPrint())
								.withResponseDefaults(prettyPrint()))
				.build();
	}

	@Test
	@DisplayName("success : image 생성 ")
	void postFile() throws Exception {

		// given
		MockMultipartFile file = new MockMultipartFile(
				"image",
				TestProperty.PNG_FILENAME,
				MediaType.IMAGE_PNG_VALUE,
				resourceLoader.getResource(TestProperty.PNG_CLASSPATH).getInputStream());

		ImageEntity imageEntity = ImageEntity.builder()
				.id(1L)
				.memberId(0L)
				.path(CommonUtils.generateS3PrefixKey() + CommonUtils.generateRandomFilename(file.getOriginalFilename()))
				.build();
		Field createDatetime = imageEntity.getClass().getSuperclass().getDeclaredField("createDatetime");
		createDatetime.setAccessible(true);
		createDatetime.set(imageEntity, LocalDateTime.now());
		Field updateDateTime = imageEntity.getClass().getSuperclass().getDeclaredField("updateDatetime");
		updateDateTime.setAccessible(true);
		updateDateTime.set(imageEntity, LocalDateTime.now());

		given(imageService.save(any(ImagePostRequest.class))).willReturn(imageEntity);

		// when
		ResultActions resultActions = mockMvc.perform(multipart(Url.IMAGE)
						.file(file))
				.andDo(print())
				.andDo(document("post-file",
						requestParts(partWithName("image").description("file parameter name")),
						relaxedResponseFields(
								fieldWithPath("data.imageId").description("파일 고유번호"),
								fieldWithPath("data.memberId").description("회원 고유번호"),
								fieldWithPath("data.type").description("파일 타입"),
								fieldWithPath("data.targetId").description("타겟 도메인 ID"),
								fieldWithPath("data.path").description("파일 URL"),
								fieldWithPath("data.createDatetime").description("생성 날짜"),
								fieldWithPath("data.updateDatetime").description("수정 날짜")
						)
				));

		// then
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
		resultActions.andExpect(jsonPath("data.imageId").isNotEmpty());
		resultActions.andExpect(jsonPath("data.memberId").isNotEmpty());
		resultActions.andExpect(jsonPath("data.type").isEmpty());
		resultActions.andExpect(jsonPath("data.targetId").isEmpty());
		resultActions.andExpect(jsonPath("data.path").isNotEmpty());
		resultActions.andExpect(jsonPath("data.createDatetime").isNotEmpty());
		resultActions.andExpect(jsonPath("data.updateDatetime").isNotEmpty());
	}

	@Test
	@DisplayName("fail : image 가 누락된 경우 ")
	void postFileIsFileNull() throws Exception {

		// given
		
		
		// when
		ResultActions resultActions = mockMvc.perform(post(Url.IMAGE))
				.andDo(print());

		// then
		resultActions.andExpect(status().isBadRequest());
		resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
		resultActions.andExpect(jsonPath("success").value(Boolean.FALSE));
		resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0001.name()));
	}

	@Test
	@DisplayName("fail : 허용되지 않은 file 을 올린 경우 ")
	void postFileIsNotAllowed() throws Exception {

		// given
		MockMultipartFile file = new MockMultipartFile(
				"image",
				TestProperty.TXT_FILENAME,
				MediaType.TEXT_PLAIN_VALUE,
				resourceLoader.getResource(TestProperty.TXT_CLASSPATH).getInputStream());

		// when
		ResultActions resultActions = mockMvc.perform(multipart(Url.IMAGE)
						.file(file))
				.andDo(print());

		// then
		resultActions.andExpect(status().isBadRequest());
		resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
		resultActions.andExpect(jsonPath("success").value(Boolean.FALSE));
		resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0001.name()));
	}
}