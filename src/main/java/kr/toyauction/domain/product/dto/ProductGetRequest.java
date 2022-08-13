package kr.toyauction.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductGetRequest {
    private Long productId;
    private String productName;
}