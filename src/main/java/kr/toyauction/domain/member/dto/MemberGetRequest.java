package kr.toyauction.domain.member.dto;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.toyauction.domain.member.entity.QMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static kr.toyauction.global.util.QueryDslExpressionUtil.contains;
import static kr.toyauction.global.util.QueryDslExpressionUtil.eq;

@Getter
@Setter
@Builder
public class MemberGetRequest {

	private Long id;
	private String username;

	public BooleanBuilder where(QMember entity) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.orAllOf(new BooleanExpression[]{
				eq(entity.id, this.id),
				contains(entity.username, this.username)
		});
		return booleanBuilder;
	}
}
