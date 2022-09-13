package kr.toyauction.domain.product.dto;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.toyauction.domain.product.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static kr.toyauction.global.util.QueryDslExpressionUtil.*;

@Getter
@Setter
@Builder
public class ProductGetRequest {

	private Long productId;
	private String productName;
	private Integer startMaxBidPrice;
	private Integer endMaxBidPrice;
	private Integer startRightPrice;
	private Integer endRightPrice;
	private Integer startMinBidPrice;
	private Integer endMinBidPrice;
	private PurchaseTime purchaseTime;
	private DeliveryOption deliveryOption;
	private ExchangeType exchangeType;
	private ProductCondition productCondition;

	public BooleanBuilder where(QProduct entity) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.orAllOf(new BooleanExpression[]{
				contains(entity.productName, this.productName),
				between(entity.rightPrice, this.startRightPrice, this.endRightPrice),
				between(entity.minBidPrice, this.startMinBidPrice, this.endMinBidPrice),
				eq(entity.purchaseTime, this.purchaseTime),
				eq(entity.deliveryOption, this.deliveryOption),
				eq(entity.exchangeType, this.exchangeType),
				eq(entity.productCondition, this.productCondition)
		});
		return booleanBuilder;
	}

	public BooleanBuilder having(QBid entity) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.orAllOf(new BooleanExpression[]{
				between(entity.bidPrice.max(), this.startMaxBidPrice, this.endMaxBidPrice)
		});
		return booleanBuilder;
	}
}