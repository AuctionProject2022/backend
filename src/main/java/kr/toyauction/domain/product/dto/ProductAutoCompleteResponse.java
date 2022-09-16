package kr.toyauction.domain.product.dto;

import kr.toyauction.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

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
