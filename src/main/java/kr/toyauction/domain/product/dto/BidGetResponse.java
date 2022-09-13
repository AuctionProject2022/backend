package kr.toyauction.domain.product.dto;

import kr.toyauction.domain.product.entity.Bid;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidGetResponse {

    private Long bidId;
    private Integer bidSeq;
    private Integer bidPrice;
    private LocalDateTime bidDatetime;

    public BidGetResponse(final Bid bid) {
        this.bidId = bid.getId();
        this.bidPrice = bid.getBidPrice();
        this.bidDatetime = bid.getCreateDatetime();
    }

}
