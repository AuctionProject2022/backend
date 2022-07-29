package kr.toyauction.domain.product.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPostRequest {
    @NotNull
    private Integer bidPrice;
}
