package kr.toyauction.domain.alert.controller;

import kr.toyauction.domain.alert.dto.AlertGetResponse;
import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.domain.alert.service.AlertService;
import kr.toyauction.global.entity.AlertCode;
import kr.toyauction.global.property.TestProperty;
import kr.toyauction.global.property.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class AlertControllerTest {

	private MockMvc mockMvc;

	@MockBean
	AlertService alertService;

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
				.apply(springSecurity())
				.build();
	}


	@Test
	@DisplayName("success : 알림 목록 조회")
	void getAlerts() throws Exception {

		//give
		int page = 0, size = 20;

		Alert alert = Alert.builder()
				.id(1L)
				.memberId(1L)
				.title("제목")
				.contents("내용")
				.code(AlertCode.AC0007)
				.url("/products/1")
				.alertRead(false)
				.endDatetime(LocalDateTime.now())
				.build();

		Field createDatetime = alert.getClass().getSuperclass().getDeclaredField("createDatetime");
		createDatetime.setAccessible(true);
		createDatetime.set(alert, LocalDateTime.now());
		Field updateDateTime = alert.getClass().getSuperclass().getDeclaredField("updateDatetime");
		updateDateTime.setAccessible(true);
		updateDateTime.set(alert, LocalDateTime.now());

		AlertGetResponse alertGetResponse = new AlertGetResponse(alert);
		List<AlertGetResponse> alertList = new ArrayList<>();
		alertList.add(alertGetResponse);
		Page<AlertGetResponse> responses = new PageImpl<>(alertList);

		given(alertService.getAlerts(any(),any())).willReturn(responses);

		//when
		ResultActions resultActions = mockMvc.perform(get(Url.ALERT+"?page={page}&size={size}",page,size)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + TestProperty.TEST_ACCESS_TOKEN)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("get-alerts",
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
						),
						relaxedResponseFields(
								fieldWithPath("data.content[].alertId").description("알림 번호"),
								fieldWithPath("data.content[].createDatetime").description("알림 생성일"),
								fieldWithPath("data.content[].endDateTime").description("종료 시간"),
								fieldWithPath("data.content[].title").description("알림 제목"),
								fieldWithPath("data.content[].contents").description("알림 내용"),
								fieldWithPath("data.content[].code").description("알림 코드"),
								fieldWithPath("data.content[].url").description("알림 선택시 이동될 url"),
								fieldWithPath("data.content[].alertRead").description("알림 읽음 여부")
						)
				));

		// then
		resultActions.andExpect(status().isOk());
		resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
	}
	@Test
	@DisplayName("fail : 알림 목록 조회 - 토큰이 없을 때")
	void getAlertsNullToken() throws Exception {
		//give

		//when
		ResultActions resultActions = mockMvc.perform(get(Url.ALERT)
						.contentType(MediaType.APPLICATION_JSON_VALUE));

		// then
		resultActions.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("success : 알림 확인")
	void postAlert() throws Exception {

		Long alertId = 125L;
		mockMvc.perform(post(Url.ALERT + "/{alertId}", alertId)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + TestProperty.TEST_ACCESS_TOKEN)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("post-alert",
						pathParameters(
								parameterWithName("alertId").description("알림 번호")
						),
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
						),
						relaxedResponseFields(
								fieldWithPath("success").description("성공 여부")
						)
				));
	}

}