package kr.toyauction.domain.product.entity;

import kr.toyauction.global.entity.BaseEntity;
import kr.toyauction.global.entity.EntitySupport;
import kr.toyauction.global.exception.DomainValidationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Bid extends BaseEntity implements EntitySupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    private Integer bidPrice;

    private Long registerMemberId;

    @Override
    public void validation() {

        if (bidPrice <= 0) {
            log.error("bidPrice : {}", bidPrice);
            throw new DomainValidationException();
        }

    }
}
