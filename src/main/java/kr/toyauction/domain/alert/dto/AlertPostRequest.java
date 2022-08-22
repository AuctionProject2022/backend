package kr.toyauction.domain.alert.dto;

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
public class AlertPostRequest {

	@NotNull
	private Long memberId;

	@NotBlank
	private Long domainId;

	@NotBlank
	private String title;

	@NotBlank
	private String contents;

	@NotNull
	@Enumerated(EnumType.STRING)
	private AlertCode alertCode;

	@NotBlank
	private String url;

	private String remainingTime;

	private Object[] messageList;
}
