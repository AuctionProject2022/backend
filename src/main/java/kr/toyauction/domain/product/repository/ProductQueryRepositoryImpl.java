package kr.toyauction.domain.product.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.toyauction.domain.image.dto.QThumbnailImage;
import kr.toyauction.domain.image.dto.ThumbnailImage;
import kr.toyauction.domain.product.dto.ProductGetRequest;
import kr.toyauction.domain.product.dto.ProductGetResponse;
import kr.toyauction.domain.product.dto.QProductGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static kr.toyauction.domain.image.entity.QImageEntity.imageEntity;
import static kr.toyauction.domain.product.entity.QBid.bid;
import static kr.toyauction.domain.product.entity.QProduct.product;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl extends ProductQuerySupport implements ProductQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<ProductGetResponse> page(@NonNull final Pageable pageable, final ProductGetRequest productGetRequest) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        JPAQuery<ProductGetResponse> jpaQuery = jpaQueryFactory
                .select(new QProductGetResponse(
                        product.id,
                        product.productName,
                        bid.bidPrice.max().as("maxBidPrice"),
                        product.minBidPrice,
                        product.unitPrice,
                        product.rightPrice,
                        product.endSaleDateTime
                ))
                .from(product)
                .leftJoin(bid).on(product.id.eq(bid.productId))
                .groupBy(
                        product.id,
                        product.productName,
                        product.minBidPrice,
                        product.unitPrice,
                        product.rightPrice,
                        product.endSaleDateTime
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(jpaQuery.fetch(), pageable, jpaQuery::fetchCount);
    }


}
