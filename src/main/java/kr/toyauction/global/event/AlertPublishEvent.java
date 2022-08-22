package kr.toyauction.global.event;

import kr.toyauction.global.entity.AlertCode;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlertPublishEvent {

	@NotNull
	private Long memberId;

	@NotBlank
	@Enumerated(EnumType.STRING)
	private AlertCode alertCode;

	@NotBlank
	private String message;

	@NotBlank
	private String url;

	private String remainingTime;

	private Object[] messageList;
}
