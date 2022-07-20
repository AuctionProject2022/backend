package kr.toyauction.global.event;

import lombok.*;

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
	private String message;
}
