package kr.toyauction.domain.alert.controller;

import kr.toyauction.global.property.TestProperty;
import kr.toyauction.global.property.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class AlertControllerTest {

	private MockMvc mockMvc;

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
	@DisplayName("success : 알림 목록 조회")
	void getAlerts() throws Exception {
		mockMvc.perform(get(Url.ALERT)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document("get-alerts",
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
						),
						relaxedResponseFields(
								fieldWithPath("data.content[].alertId").description("알림 번호"),
								fieldWithPath("data.content[].alertTitle").description("알림 제목"),
								fieldWithPath("data.content[].alertContents").description("알림 내용"),
								fieldWithPath("data.content[].createDatetime").description("알림 생성일"),
								fieldWithPath("data.content[].url").description("알림 선택시 이동될 url"),
								fieldWithPath("data.content[].alertRead").description("알림 읽음 여부")
						)
				));
	}

	@Test
	@DisplayName("success : 알림 확인")
	void postAlert() throws Exception {

		Long alertId = 125L;
		mockMvc.perform(post(Url.ALERT + "/{alertId}", alertId)
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