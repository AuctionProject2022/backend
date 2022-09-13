package kr.toyauction.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static kr.toyauction.domain.product.entity.QProduct.product;
import static org.springframework.util.ObjectUtils.isEmpty;

public class QueryDslExpressionUtil {

	public static <T extends Number & Comparable<?>> BooleanExpression in(
			final NumberExpression<T> expression, final Collection<T> value) {
		return CollectionUtils.isEmpty(value) ? null : expression.in(value);
	}

	public static <T extends Number & Comparable<?>> BooleanExpression eq(
			final NumberExpression<T> expression, final T value) {
		return value == null ? null : expression.eq(value);
	}

	public static <T extends Number & Comparable<?>> BooleanExpression ne(
			final NumberExpression<T> expression, final T value) {
		return value == null ? null : expression.ne(value);
	}

	public static BooleanExpression eq(final StringExpression expression, final String value) {
		return StringUtils.hasText(value) ? expression.eq(value) : null;
	}

	public static BooleanExpression ne(final StringExpression expression, final String value) {
		return StringUtils.hasText(value) ? expression.ne(value) : null;
	}

	public static BooleanExpression eq(final BooleanExpression expression, final Boolean value) {
		return value == null ? null : expression.eq(value);
	}

	public static BooleanExpression ne(final BooleanExpression expression, final Boolean value) {
		return value == null ? null : expression.ne(value);
	}

	public static <T extends Enum<T>> BooleanExpression eq(
			final EnumExpression<T> expression, final T value) {
		return value == null ? null : expression.eq(value);
	}

	public static <T extends Enum<T>> BooleanExpression ne(
			final EnumExpression<T> expression, final T value) {
		return value == null ? null : expression.ne(value);
	}

	public static <T extends Enum<T>> BooleanExpression in(
			final EnumExpression<T> expression, final List<T> value) {
		if (CollectionUtils.isEmpty(value)) {
			return null;
		}

		return expression.in(value);
	}

	public static BooleanExpression contains(final StringExpression expression, final String value) {
		return StringUtils.hasText(value) ? expression.contains(value) : null;
	}

	public static BooleanExpression compareNull(
			final StringExpression expression, final Boolean value) {
		if (value == null) {
			return null;
		}

		return value ? expression.isNotNull() : expression.isNull();
	}

	public static BooleanExpression exists(final StringExpression expression, final Boolean value) {
		if (value == null) {
			return null;
		}

		return value
				? expression.isNotNull().and(expression.isNotEmpty())
				: expression.isNull().or(expression.isEmpty());
	}

	public static <T extends Number & Comparable<?>> BooleanExpression exists(
			final NumberExpression<T> expression, final Boolean value) {
		if (value == null) {
			return null;
		}

		return value ? expression.isNotNull() : expression.isNull();
	}

	public static BooleanExpression between(
			final DateTimeExpression<LocalDateTime> expression,
			final LocalDate from,
			final LocalDate to) {
		if (from == null && to == null) {
			return null;
		}

		if (to == null) {
			return expression.goe(from.atStartOfDay());
		}

		return from == null
				? expression.loe(to.atTime(LocalTime.MAX))
				: expression.between(from.atStartOfDay(), to.atTime(LocalTime.MAX));
	}

	public static BooleanExpression between(
			final NumberExpression<?> expression,
			final Integer min,
			final Integer max) {
		if (min == null && max == null) {
			return null;
		}

		if (max == null) {
			return expression.goe(min);
		}

		return min == null
				? expression.loe(max)
				: expression.between(min, max);
	}

	public static <T extends Number & Comparable<?>> BooleanExpression gt(
			final NumberExpression<T> expression, final T value) {
		return value == null ? null : expression.gt(value);
	}

	public static <T extends Number & Comparable<?>> BooleanExpression goe(
			final NumberExpression<T> expression, final T value) {
		return value == null ? null : expression.goe(value);
	}

	public static <T extends Number & Comparable<?>> BooleanExpression lt(
			final NumberExpression<T> expression, final T value) {
		return value == null ? null : expression.lt(value);
	}

	public static <T extends Number & Comparable<?>> BooleanExpression loe(
			final NumberExpression<T> expression, final T value) {
		return value == null ? null : expression.loe(value);
	}

	// TODO 리팩토링 예정
	public static List<OrderSpecifier> orderSpecifiers(Pageable pageable) {
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

	public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
		Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);

		return new OrderSpecifier(order, fieldPath);
	}
}
