package kr.toyauction.domain.product.entity;

import kr.toyauction.global.entity.CodeEnum;

public enum DeliveryOption implements CodeEnum {
    DELIVERY("배송가능"),
    DIRECT("직거래");

    private final String description;

    DeliveryOption(String description) {
        this.description = description;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getValue() {
        return description;
    }
}
