package kr.toyauction.domain.member.repository;

import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.member.entity.Platform;
import kr.toyauction.domain.member.entity.Role;
import kr.toyauction.global.exception.DomainNotFoundException;
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
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("success : memberRepository save()")
    void save(){
        // given
        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("testName")
                .build();

        // when
        Member saveMember = memberRepository.save(member);

        // then
        assertNotNull(saveMember.getId());
        assertEquals(member.getPlatformId(),saveMember.getPlatformId());
        assertEquals(member.getPlatform(),saveMember.getPlatform());
        assertEquals(member.getPicture(),saveMember.getPicture());
        assertEquals(member.getRole(),saveMember.getRole());
        assertEquals(member.getUsername(),saveMember.getUsername());
        assertEquals(member.getCreateDatetime(),saveMember.getCreateDatetime());
        assertEquals(member.getUpdateDatetime(),saveMember.getUpdateDatetime());
    }

    @Test
    @DisplayName("success : memberRepository findByUsername")
    void findByUsername(){
        // given
        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("testName")
                .build();

        // when
        memberRepository.save(member);
        Member getMember = memberRepository.findByUsername("testName")
                .orElseThrow();

        // then
        assertNotNull(getMember.getId());
        assertEquals(member.getPlatformId(),getMember.getPlatformId());
        assertEquals(member.getPlatform(),getMember.getPlatform());
        assertEquals(member.getPicture(),getMember.getPicture());
        assertEquals(member.getRole(),getMember.getRole());
        assertEquals(member.getUsername(),getMember.getUsername());
        assertEquals(member.getCreateDatetime(),getMember.getCreateDatetime());
        assertEquals(member.getUpdateDatetime(),getMember.getUpdateDatetime());
    }

    @Test
    @DisplayName("success : memberRepository findByPlatformId")
    void findByPlatformId(){
        // given
        Member member = Member.builder()
                .platformId("test@test.com")
                .platform(Platform.google)
                .picture("test")
                .role(Role.ROLE_USER)
                .username("testName")
                .build();

        // when
        memberRepository.save(member);
        Member getMember = memberRepository.findByPlatformId("test@test.com")
                .orElseThrow();

        // then
        assertNotNull(getMember.getId());
        assertEquals(member.getPlatformId(),getMember.getPlatformId());
        assertEquals(member.getPlatform(),getMember.getPlatform());
        assertEquals(member.getPicture(),getMember.getPicture());
        assertEquals(member.getRole(),getMember.getRole());
        assertEquals(member.getUsername(),getMember.getUsername());
        assertEquals(member.getCreateDatetime(),getMember.getCreateDatetime());
        assertEquals(member.getUpdateDatetime(),getMember.getUpdateDatetime());
    }
}
