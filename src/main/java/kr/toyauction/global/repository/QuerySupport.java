package kr.toyauction.global.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static kr.toyauction.domain.member.entity.QMember.member;
import static kr.toyauction.domain.product.entity.QProduct.product;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

public class QuerySupport {

    protected BooleanExpression memberIdEq(Long id) {
        return id != null ? member.id.eq(id) : null;
    }

    protected BooleanExpression memberUsernameContains(String username) {
        return hasText(username) ? member.username.contains(username) : null;
    }

    protected List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                OrderSpecifier<?> orderSpecifier = getSortedColumn(direction, product, order.getProperty());
                ORDERS.add(orderSpecifier);
            }
        }

        return ORDERS;
    }

    protected  OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);

        return new OrderSpecifier(order, fieldPath);
    }
}
