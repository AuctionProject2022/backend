package kr.toyauction.domain.member.repository;

import kr.toyauction.domain.member.dto.MemberGetRequest;
import kr.toyauction.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface MemberQueryRepository {

    Page<Member> page(@NonNull final Pageable pageable, final MemberGetRequest memberGetRequest);
}
