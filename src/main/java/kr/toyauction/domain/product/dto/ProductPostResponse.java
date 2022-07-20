package kr.toyauction.domain.product.dto;

import kr.toyauction.domain.product.entity.Product;
import kr.toyauction.global.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductPostResponse extends BaseEntity {

    private Long productId;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;

    public ProductPostResponse(final Product product) {
        this.productId = product.getId();
        this.createDatetime = product.getCreateDatetime();
        this.updateDatetime = product.getUpdateDatetime();
    }
}
