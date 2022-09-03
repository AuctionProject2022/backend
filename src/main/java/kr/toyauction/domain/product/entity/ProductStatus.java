package kr.toyauction.domain.product.entity;

import kr.toyauction.global.entity.CodeEnum;

public enum ProductStatus implements CodeEnum {
	ON_SALE("판매중"),
	CLOSE("판매종료");

	private final String description;

	ProductStatus(String description) {
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
