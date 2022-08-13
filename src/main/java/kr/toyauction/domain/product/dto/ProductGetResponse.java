package kr.toyauction.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.toyauction.domain.image.dto.ThumbnailImage;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.global.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductGetResponse {

    private Long productId;

    //private ThumbnailImage thumbnailImage;

    private String productName;

    private Integer maxBidPrice;

    private Integer rightPrice;

    private Integer minBidPrice;

    private Integer unitPrice;
    private LocalDateTime endSaleDateTime;

    @QueryProjection
    public ProductGetResponse(Long productId
//                            , ThumbnailImage thumbnailImage
                            , String productName
                            , Integer maxBidPrice
                            , Integer rightPrice
                            , Integer minBidPrice
                            , Integer unitPrice
                            , LocalDateTime endSaleDateTime) {
        this.productId = productId;
//        this.thumbnailImage = thumbnailImage;
        this.productName = productName;
        this.maxBidPrice = maxBidPrice;
        this.rightPrice = rightPrice;
        this.minBidPrice = minBidPrice;
        this.unitPrice = unitPrice;
        this.endSaleDateTime = endSaleDateTime;
    }
}
