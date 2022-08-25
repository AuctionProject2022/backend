package kr.toyauction.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PurchaseTime {
    SIX_MONTHS("6개월 이내"),
    ONE_TO_TWO_YEARS("1~2년 이내"),
    TWO_YEARS_OVER("2년 이상");
    // NO_REMEMBER("기억안남");

    private final String description;

    PurchaseTime(String description) {
        this.description = description;
    }

}
