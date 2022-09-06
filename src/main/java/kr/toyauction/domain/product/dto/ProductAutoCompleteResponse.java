package kr.toyauction.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.toyauction.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductAutoCompleteResponse {
    private Long productId;
    private String productName;

    public ProductAutoCompleteResponse(Product product){
        this.productId = product.getId();
        this.productName = product.getProductName();
    }
}
