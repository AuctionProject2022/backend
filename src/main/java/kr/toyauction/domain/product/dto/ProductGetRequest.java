package kr.toyauction.domain.product.dto;

import kr.toyauction.domain.product.entity.DeliveryOption;
import kr.toyauction.domain.product.entity.ExchangeType;
import kr.toyauction.domain.product.entity.ProductCondition;
import kr.toyauction.domain.product.entity.PurchaseTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

}