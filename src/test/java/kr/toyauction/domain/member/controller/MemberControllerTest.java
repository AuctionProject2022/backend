package kr.toyauction.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.toyauction.domain.member.dto.MemberPatchRequest;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.service.MemberService;
import kr.toyauction.global.error.GlobalErrorCode;
import kr.toyauction.global.property.TestProperty;
import kr.toyauction.global.property.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class})
public class MemberControllerTest {

    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("username 으로 member 검색")
    void getMemberByUsername() throws Exception{

        // given
        String username = "testUser";
        Member member = Member.builder()
                .id(1L)
                .username(username)
                .build();

        Field createDatetime = member.getClass().getSuperclass().getDeclaredField("createDatetime");
        createDatetime.setAccessible(true);
        createDatetime.set(member, LocalDateTime.now());
        Field updateDateTime = member.getClass().getSuperclass().getDeclaredField("updateDatetime");
        updateDateTime.setAccessible(true);
        updateDateTime.set(member, LocalDateTime.now());

        given(memberService.getMemberByUsername(any())).willReturn(member);

        // when
        ResultActions resultActions = mockMvc.perform(get(Url.MEMBER + "/username/{username}", username)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andDo(document("get-member-username",
                        pathParameters(
                                parameterWithName("username").description("닉네임")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("data.memberId").description("유저 고유번호"),
                                fieldWithPath("data.username").description("닉네임"),
                                fieldWithPath("data.createDatetime").description("등록일"),
                                fieldWithPath("data.updateDatetime").description("수정일"),
                                fieldWithPath("data.enabled").description("사용 여부")
                        )
                ));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(jsonPath("data.memberId").isNotEmpty());
        resultActions.andExpect(jsonPath("data.username").isNotEmpty());
        resultActions.andExpect(jsonPath("data.createDatetime").isNotEmpty());
        resultActions.andExpect(jsonPath("data.updateDatetime").isNotEmpty());
        resultActions.andExpect(jsonPath("data.enabled").isNotEmpty());
    }

    @Test
    @DisplayName("username 이 1글자로 들어오는 경우")
    void getMemberByUsernameMinText() throws Exception{
        // given
        String username = "t";

        // when
        ResultActions resultActions = mockMvc.perform(get(Url.MEMBER + "/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(jsonPath("success").value(Boolean.FALSE));
        resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0003.name()));
    }

    @Test
    @DisplayName("username 이 11글자로 들어오는 경우")
    void getMemberByUsernameMaxText() throws Exception{
        // given
        String username = "testUsernam";

        // when
        ResultActions resultActions = mockMvc.perform(get(Url.MEMBER + "/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(jsonPath("success").value(Boolean.FALSE));
        resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0003.name()));
    }

    @Test
    @DisplayName("username 에 특수문자가 들어오는 경우")
    void getMemberByUsernameSpecialText() throws Exception{
        // given
        String username = "test!";

        // when
        ResultActions resultActions = mockMvc.perform(get(Url.MEMBER + "/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(jsonPath("success").value(Boolean.FALSE));
        resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0003.name()));
    }
    @Test
    @DisplayName("username 에 띄어쓰기가 있는 경우")
    void getMemberByUsernameSpacingText() throws Exception{
        // given
        String username = "test Test";

        // when
        ResultActions resultActions = mockMvc.perform(get(Url.MEMBER + "/username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(jsonPath("success").value(Boolean.FALSE));
        resultActions.andExpect(jsonPath("code").value(GlobalErrorCode.G0003.name()));
    }

    @Test
    @DisplayName("유저정보 수정")
    void patchMember() throws Exception{

        // given
        Long memberId = 1L;
        MemberPatchRequest request = new MemberPatchRequest();
        request.setUsername("testUser");

        // when
        ResultActions resultActions = mockMvc.perform(patch(Url.MEMBER + "/{memberId}", memberId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TestProperty.TEST_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andDo(document("patch-member",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유 아이디")
                        )
                ));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }
}
