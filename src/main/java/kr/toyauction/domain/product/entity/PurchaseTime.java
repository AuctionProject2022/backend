package kr.toyauction.domain.product.entity;

import kr.toyauction.global.entity.CodeEnum;

public enum PurchaseTime implements CodeEnum {
    SIX_MONTHS("6개월 이내"),
    ONE_TO_TWO_YEARS("1~2년 이내"),
    TWO_YEARS_OVER("2년 이상");

    private final String description;

    PurchaseTime(String description) {
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
