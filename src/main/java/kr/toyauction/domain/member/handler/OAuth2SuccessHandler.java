package kr.toyauction.domain.member.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.toyauction.domain.member.dto.SocialResponse;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.entity.Platform;
import kr.toyauction.domain.member.entity.Role;
import kr.toyauction.domain.member.repository.MemberRepository;
import kr.toyauction.global.dto.SuccessResponse;
import kr.toyauction.global.dto.Token;
import kr.toyauction.global.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        Member member = memberRepository.findByPlatformId((String) oAuth2User.getAttributes().get("email")); // 메일로 db 검색 추후 타 플랫폼이 추가 되면 검색 조건에 플랫폼까지 추가해서 검색 해야함

        if (member == null) {
            String username = RandomStringUtils.randomAlphanumeric(10); // 랜덤 닉네임 생성
            member = memberRepository.findByUsername(username); // 닉네임으로 db 검색

            while (member != null) { // 닉네임으로 검색된 값이 있으면 랜덤 닉네임 다시 생성해서 검색 없을때 까지
                username = RandomStringUtils.randomAlphanumeric(10);
                member = memberRepository.findByUsername(username);
            }

            member = Member.builder()
                    .platformId((String) oAuth2User.getAttributes().get("email"))
                    .username(username)
                    .picture((String) oAuth2User.getAttributes().get("picture"))
                    .platform(Platform.google)  // 추후 타 플랫폼 추가시 변경 되어야 함
                    .role(Role.USER)
                    .build();

            memberRepository.save(member);  // 신규 멤버 저장
        }
        Token token = jwtProvider.createToken(member.getId(),authentication); // 토큰 발행
        member.setRefreshToken(token.getRefreshToken());      // 리프레쉬 토큰값 db에 업데이트
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken()) // 헤더에 전달할 쿠키 설정
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie",cookie.toString());
        response.setContentType("application/json;charset=UTF-8");
        SocialResponse socialResponse = new SocialResponse(token.getAccessToken(),true); // 바디 응답 data
        SuccessResponse<SocialResponse> successResponse = new SuccessResponse<>(socialResponse);
        String result = mapper.writeValueAsString(successResponse);
        response.getWriter().write(result);
    }
}
