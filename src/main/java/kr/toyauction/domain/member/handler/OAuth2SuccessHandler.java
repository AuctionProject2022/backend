package kr.toyauction.domain.member.handler;

import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.entity.Platform;
import kr.toyauction.domain.member.entity.Role;
import kr.toyauction.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
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
    }
}
