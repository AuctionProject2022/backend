package kr.toyauction.domain.member.repository;

import kr.toyauction.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPlatformId(String platformId);
    Optional<Member> findByUsername(String username);
}
