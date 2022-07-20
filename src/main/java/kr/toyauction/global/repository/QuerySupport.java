package kr.toyauction.global.repository;

import com.querydsl.core.types.dsl.BooleanExpression;

import static kr.toyauction.domain.member.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

public class QuerySupport {

    protected BooleanExpression memberIdEq(Long id) {
        return id != null ? member.id.eq(id) : null;
    }

    protected BooleanExpression memberUsernameContains(String username) {
        return hasText(username) ? member.username.contains(username) : null;
    }
}
