package kr.toyauction.domain.product.dto;

import kr.toyauction.domain.product.entity.DeliveryOption;
import kr.toyauction.domain.product.entity.ExchangeType;
import kr.toyauction.domain.product.entity.ProductCondition;
import kr.toyauction.domain.product.entity.PurchaseTime;
import kr.toyauction.global.property.Regex;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPostRequest {

    @NotBlank
    @Pattern(regexp = Regex.PRODUCTNAME, message = "{REGEX_PRODUCT_NAME}")
    private String productName;

    private List<Long> imageIds;

    @NotNull
    private Long thumbnailImageId;

    @NotNull
    @Min(0)
    private Integer minBidPrice;

    @NotNull
    @Min(0)
    private Integer rightPrice;

    @NotNull
    private LocalDateTime startSaleDateTime;

    @NotNull
    private LocalDateTime endSaleDateTime;

    @NotNull
    private Integer unitPrice;

    @NotNull
    private PurchaseTime purchaseTime;

    @NotNull
    private DeliveryOption deliveryOption;

    @NotNull
    private ExchangeType exchangeType;

    @NotNull
    private ProductCondition productCondition;

    @NotBlank
    private String detail;
}
