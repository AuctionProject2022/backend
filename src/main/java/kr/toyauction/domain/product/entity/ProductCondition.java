package kr.toyauction.domain.product.entity;

import kr.toyauction.global.entity.CodeEnum;

public enum ProductCondition implements CodeEnum {
    NEW("새 제품"),
    CLEAN("깨끗한 편"),
    USED("사용감 있음"),
    REPAIR_REQUIRED("수리필요");

    private final String description;

    ProductCondition(String description) {
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
