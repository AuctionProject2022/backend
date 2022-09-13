package kr.toyauction.domain.product.dto;

import kr.toyauction.domain.image.dto.ImageDto;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.product.entity.*;
import kr.toyauction.global.dto.BaseResponse;
import kr.toyauction.global.dto.EnumCodeValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ProductViewResponse extends BaseResponse {

    private Long productId;
    private String productName;
    private List<ImageDto> images;
    private Integer minBidPrice;
    private Integer rightPrice;
    private Integer maxBidPrice;
    private LocalDateTime startSaleDateTime;
    private LocalDateTime endSaleDateTime;
    private Integer unitPrice;
    private EnumCodeValue purchaseTime;
    private EnumCodeValue deliveryOption;
    private EnumCodeValue exchangeType;
    private EnumCodeValue productCondition;
    private EnumCodeValue productStatus;
    private String detail;
    private Long registerMemberId;
    private List<BidGetResponse> bids;

    public ProductViewResponse(final Product product) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.minBidPrice = product.getMinBidPrice();
        this.rightPrice = product.getRightPrice();
        this.startSaleDateTime = product.getStartSaleDateTime();
        this.endSaleDateTime = product.getEndSaleDateTime();
        this.unitPrice = product.getUnitPrice();
        this.purchaseTime =new EnumCodeValue(product.getPurchaseTime());
        this.deliveryOption = new EnumCodeValue(product.getDeliveryOption());
        this.exchangeType = new EnumCodeValue(product.getExchangeType());
        this.productCondition = new EnumCodeValue(product.getProductCondition());
        this.productStatus = new EnumCodeValue(product.getProductStatus());
        this.detail = product.getDetail();
        this.registerMemberId = product.getRegisterMemberId();
        this.createDatetime = product.getCreateDatetime();
        this.updateDatetime = product.getUpdateDatetime();
    }

    public void setBids(List<Bid> bids){
        this.bids = bids.size() == 0 ? null : bids.stream().map(BidGetResponse::new).collect(Collectors.toList());
        this.maxBidPrice = bids.size() == 0 ? null : bids.stream().max(Comparator.comparing(Bid::getBidPrice)).get().getBidPrice();
    }

    public void setImages(List<ImageEntity> images){
        this.images = images.size() == 0 ? null : images.stream().map(ImageDto::new).collect(Collectors.toList());
    }

}








