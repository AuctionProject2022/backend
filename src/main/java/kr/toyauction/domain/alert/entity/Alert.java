package kr.toyauction.domain.alert.entity;


import kr.toyauction.global.entity.BaseEntity;
import kr.toyauction.global.entity.EntitySupport;
import kr.toyauction.global.exception.DomainValidationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Alert extends BaseEntity implements EntitySupport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	private String message;

	@Override
	public void validation() {

		if (memberId == null) {
			log.error("memberId : {}", memberId);
			throw new DomainValidationException();
		}

		if (message == null || message.isBlank()) {
			log.error("message : {}", message);
			throw new DomainValidationException();
		}
	}
}
