package kr.toyauction.global.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseResponse {

	protected LocalDateTime createDatetime;
	protected LocalDateTime updateDatetime;
}
