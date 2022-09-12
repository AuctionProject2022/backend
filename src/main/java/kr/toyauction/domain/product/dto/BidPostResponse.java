package kr.toyauction.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.toyauction.domain.product.entity.Bid;
import kr.toyauction.global.dto.BaseResponse;
import kr.toyauction.global.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidPostResponse{

    private Long bidId;
    private Integer bidPrice;
    private LocalDateTime createDatetime;

    public BidPostResponse(final Bid bid) {
        this.bidId = bid.getId();
        this.bidPrice = bid.getBidPrice();
        this.createDatetime = bid.getCreateDatetime();
    }
}
