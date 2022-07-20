package kr.toyauction.domain.alert.dto;

import lombok.*;

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
	private String message;
}
