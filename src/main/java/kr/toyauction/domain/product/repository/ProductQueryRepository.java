package kr.toyauction.domain.product.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.toyauction.domain.image.dto.QImageDto;
import kr.toyauction.domain.image.entity.ImageType;
import kr.toyauction.domain.product.dto.ProductGetRequest;
import kr.toyauction.domain.product.dto.ProductGetResponse;
import kr.toyauction.domain.product.dto.QProductGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import static kr.toyauction.domain.image.entity.QImageEntity.imageEntity;
import static kr.toyauction.domain.product.entity.QBid.bid;
import static kr.toyauction.domain.product.entity.QProduct.product;
import static kr.toyauction.global.util.QueryDslExpressionUtil.orderSpecifiers;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Page<ProductGetResponse> page(@NonNull final Pageable pageable, final ProductGetRequest productGetRequest) {
		JPAQuery<ProductGetResponse> jpaQuery = jpaQueryFactory
				.select(new QProductGetResponse(
						product.id,
						new QImageDto(imageEntity.id, imageEntity.path, imageEntity.type),
						product.productName,
						bid.bidPrice.max().as("maxBidPrice"),
						product.rightPrice,
						product.minBidPrice,
						product.unitPrice,
						product.endSaleDateTime
				))
				.from(product)
				.leftJoin(imageEntity).on(product.id.eq(imageEntity.targetId).and(imageEntity.type.eq(ImageType.PRODUCT_THUMBNAIL)))
				.leftJoin(bid).on(product.id.eq(bid.product.id))
				.where(productGetRequest.where(product))
				.groupBy(
						product.id,
						product.productName,
						product.minBidPrice,
						product.unitPrice,
						product.rightPrice,
						product.endSaleDateTime
				)
				.having(productGetRequest.having(bid))
				.orderBy(orderSpecifiers(pageable).toArray(OrderSpecifier[]::new))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize());

		return PageableExecutionUtils.getPage(jpaQuery.fetch(), pageable, jpaQuery::fetchCount);
	}
}
