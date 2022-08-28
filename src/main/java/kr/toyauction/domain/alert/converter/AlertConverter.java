package kr.toyauction.domain.alert.converter;


import kr.toyauction.domain.alert.dto.AlertPostRequest;
import kr.toyauction.global.event.AlertPublishEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
public class AlertConverter {

	private final MessageSource messageSource;

	/**
	 * alertPublishEvent to AlertPostRequest convert
	 */
	public static AlertPostRequest to(final AlertPublishEvent alertPublishEvent) {
		return AlertPostRequest.builder()
				.memberId(alertPublishEvent.getMemberId())
				.alertCode(alertPublishEvent.getAlertCode())
				.contents(alertPublishEvent.getMessage())
				.url(alertPublishEvent.getUrl())
				.endDatetime(alertPublishEvent.getEndDatetime())
				.messageList(alertPublishEvent.getMessageList())
				.build();
	}
}
