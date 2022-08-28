package kr.toyauction.domain.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.toyauction.domain.product.dto.ProductGetRequest;
import kr.toyauction.domain.product.entity.*;
import kr.toyauction.global.repository.QuerySupport;
import org.springframework.lang.NonNull;

import static kr.toyauction.domain.product.entity.QBid.bid;
import static kr.toyauction.domain.product.entity.QProduct.product;

public class ProductQuerySupport extends QuerySupport {

    protected BooleanBuilder where(@NonNull final ProductGetRequest productGetRequest) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.orAllOf(new BooleanExpression[]{
                productNameContains(productGetRequest.getProductName()),
                rightPriceRange(productGetRequest.getStartRightPrice(), productGetRequest.getEndRightPrice()),
                minBidPriceRange(productGetRequest.getStartMinBidPrice(), productGetRequest.getEndMinBidPrice()),
                purchaseTimeEq(productGetRequest.getPurchaseTime()),
                deliveryOptionEq(productGetRequest.getDeliveryOption()),
                exchangeTypeEq(productGetRequest.getExchangeType()),
                productConditionEq(productGetRequest.getProductCondition())
        });
        return booleanBuilder;
    }

    protected BooleanBuilder having(@NonNull final ProductGetRequest productGetRequest) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.orAllOf(new BooleanExpression[]{
                maxBidPriceRange(productGetRequest.getStartMaxBidPrice(), productGetRequest.getEndMaxBidPrice())
        });
        return booleanBuilder;
    }

    private BooleanExpression maxBidPriceRange(Integer startMaxBidPrice, Integer endMaxBidPrice) {
        return startMaxBidPrice != null && endMaxBidPrice != null ? bid.bidPrice.max().between(startMaxBidPrice, endMaxBidPrice) : null;
    }

    private BooleanExpression productConditionEq(ProductCondition productCondition) {
        return productCondition != null ? product.productCondition.eq(productCondition) : null;
    }

    private BooleanExpression exchangeTypeEq(ExchangeType exchangeType) {
        return exchangeType != null ? product.exchangeType.eq(exchangeType) : null;
    }

    private BooleanExpression deliveryOptionEq(DeliveryOption deliveryOption) {
        return deliveryOption != null ? product.deliveryOption.eq(deliveryOption) : null;
    }

    private BooleanExpression purchaseTimeEq(PurchaseTime purchaseTime) {
        return purchaseTime != null ? product.purchaseTime.eq(purchaseTime) : null;
    }

    private BooleanExpression minBidPriceRange(Integer startMinBidPrice, Integer endMinBidPrice) {
        return startMinBidPrice != null && endMinBidPrice != null ? product.minBidPrice.between(startMinBidPrice, endMinBidPrice) : null;
    }

    private BooleanExpression rightPriceRange(Integer startRightPrice, Integer endRightPrice) {
        return startRightPrice != null && endRightPrice != null ? product.rightPrice.between(startRightPrice, endRightPrice) : null;
    }

    private BooleanExpression productNameContains(String productName) {
        return productName != null ? product.productName.containsIgnoreCase(productName) : null;
    }



}
