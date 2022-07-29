package kr.toyauction.domain.product.dto;

import kr.toyauction.domain.product.entity.Bid;
import kr.toyauction.global.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidPostResponse extends BaseEntity {

    private Long id;
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
    protected boolean enabled;

    public BidPostResponse(final Bid bid) {
        this.id = bid.getId();
        this.createDatetime = bid.getCreateDatetime();
        this.updateDatetime = bid.getUpdateDatetime();
        this.enabled = bid.isEnabled();
    }
}
