package kr.toyauction.domain.member.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.toyauction.domain.member.dto.MemberGetRequest;
import kr.toyauction.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import static kr.toyauction.domain.member.entity.QMember.member;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl extends MemberQuerySupport implements MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Member> page(@NonNull final Pageable pageable, final MemberGetRequest memberGetRequest) {
        JPAQuery<Member> jpaQuery = jpaQueryFactory
                .selectFrom(member)
                .where(where(memberGetRequest));
        return PageableExecutionUtils.getPage(jpaQuery.fetch(), pageable, jpaQuery::fetchCount);
    }
}
