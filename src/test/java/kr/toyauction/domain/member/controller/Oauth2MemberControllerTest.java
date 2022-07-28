package kr.toyauction.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.toyauction.domain.member.dto.SocialResponse;
import kr.toyauction.global.dto.SuccessResponse;
import kr.toyauction.global.property.TestProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
class Oauth2MemberControllerTest {

	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

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
	@DisplayName("success : 구글 로그인 성공 이후 jwt 토큰 발급")
	void googleLogin() throws Exception {


		mockMvc.perform(get("/oauth2/authorization/google")
						.with(oidcLogin()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andDo(document("oauth2-google",
						responseHeaders(
								headerWithName(HttpHeaders.LOCATION).description("구글 로그인 페이지 리디렉션")
						)
				));
	}

}