package kr.toyauction.domain.product.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidPostRequest {
    @NotNull
    @Min(0)
    @Max(1000000000)
    private Integer bidPrice;
}
