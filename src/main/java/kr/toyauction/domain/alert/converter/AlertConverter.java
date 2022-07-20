package kr.toyauction.domain.alert.converter;


import kr.toyauction.domain.alert.dto.AlertPostRequest;
import kr.toyauction.global.event.AlertPublishEvent;

public class AlertConverter {

	/**
	 * alertPublishEvent to AlertPostRequest convert
	 */
	public static AlertPostRequest to(final AlertPublishEvent alertPublishEvent) {
		return AlertPostRequest.builder()
				.memberId(alertPublishEvent.getMemberId())
				.message(alertPublishEvent.getMessage())
				.build();
	}
}
