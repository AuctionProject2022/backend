package kr.toyauction.domain.member.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.toyauction.domain.member.dto.MemberGetRequest;
import kr.toyauction.global.repository.QuerySupport;
import org.springframework.lang.NonNull;

public class MemberQuerySupport extends QuerySupport {

    protected BooleanBuilder where(@NonNull final MemberGetRequest memberGetRequest) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.orAllOf(new BooleanExpression[]{

                memberIdEq(memberGetRequest.getId()),
                memberUsernameContains(memberGetRequest.getUsername())
        });
        return booleanBuilder;
    }
}
