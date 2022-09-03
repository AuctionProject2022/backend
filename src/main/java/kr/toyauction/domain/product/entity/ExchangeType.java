package kr.toyauction.domain.product.entity;

import kr.toyauction.global.entity.CodeEnum;

public enum ExchangeType implements CodeEnum {
    POSSIBLE("교환가능"),
    IMPOSSIBLE("교환불가");

    private final String description;

    ExchangeType(String description) {
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
