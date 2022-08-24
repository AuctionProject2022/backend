package kr.toyauction.domain.member.service;

import kr.toyauction.domain.member.dto.MemberPatchRequest;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.entity.Platform;
import kr.toyauction.domain.member.entity.Role;
import kr.toyauction.domain.member.repository.MemberRepository;
import kr.toyauction.global.exception.DomainNotFoundException;
import kr.toyauction.global.exception.OverlapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@ExtendWith({MockitoExtension.class})
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService memberService;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository);
    }

    @Test
    @DisplayName("success : username 으로 유저 정보 조회")
    void getMemberByUsername(){
        // given
        String username = "test";

        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username(username)
                .build();

        memberRepository.save(member);

        // when
        Member memberByUsername = memberService.getMemberByUsername(username);

        // then
        assertNotNull(memberByUsername.getId());
        assertEquals(member.getPlatformId(),memberByUsername.getPlatformId());
        assertEquals(member.getPlatform(),memberByUsername.getPlatform());
        assertEquals(member.getPicture(),memberByUsername.getPicture());
        assertEquals(member.getRole(),memberByUsername.getRole());
        assertEquals(member.getUsername(),memberByUsername.getUsername());
        assertEquals(member.getCreateDatetime(),memberByUsername.getCreateDatetime());
        assertEquals(member.getUpdateDatetime(),memberByUsername.getUpdateDatetime());
    }

    @Test
    @DisplayName("fail : username 의 유저 정보가 존재 하지 않을 때")
    void getMemberByUsernameNull(){
        // given
        String username = "test";

        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("testNull")
                .build();

        memberRepository.save(member);

        // when
        assertThrows(DomainNotFoundException.class, () -> memberService.getMemberByUsername(username));
    }

    @Test
    @DisplayName("success : 유저정보 수정")
    void patchMember(){
        // given
        String username = "change";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .username(username)
                .build();

        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("existing")
                .build();

        member = memberRepository.save(member);
        Long memberId = member.getId();

        // when
        memberService.patchMember(memberId,request);
        Member resultMember = memberRepository.getById(memberId);

        // then
        assertNotNull(resultMember.getId());
        assertEquals(resultMember.getUsername(),username);
    }

    @Test
    @DisplayName("fail : 유저정보 수정 - 동일 닉네임 존재")
    void patchMemberOverlap(){
        // given
        Long memberId = 1L;
        String username = "overlap";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .username(username)
                .build();

        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("existing")
                .build();

        Member overlapMember = Member.builder()
                .platformId("test@overlap.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("overlap")
                .build();

        memberRepository.save(member);
        memberRepository.save(overlapMember);

        // when
        assertThrows(OverlapException.class, () -> memberService.patchMember(memberId,request));
    }

    @Test
    @DisplayName("fail : 유저정보 수정 - 없는 회원 id")
    void patchMemberNull(){
        // given
        Long memberId = Long.MIN_VALUE;
        String username = "change";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .username(username)
                .build();

        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("existing")
                .build();

        memberRepository.save(member);

        // when
        assertThrows(DomainNotFoundException.class, () -> memberService.patchMember(memberId,request));
    }
}
