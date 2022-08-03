package kr.toyauction.domain.alert.entity;


import kr.toyauction.global.entity.BaseEntity;
import kr.toyauction.global.entity.EntitySupport;
import kr.toyauction.global.exception.DomainValidationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Alert extends BaseEntity implements EntitySupport {

	@Id
	@Column(name = "AlertId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private String title;

	private String contents;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AlertCode code;

	@Column(nullable = false)
	private String url;

	@Override
	public void validation() {

		if (memberId == null) {
			log.error("memberId : {}", memberId);
			throw new DomainValidationException();
		}

		if (title == null || title.isBlank()) {
			log.error("message : {}", title);
			throw new DomainValidationException();
		}

		if (url == null || url.isBlank()) {
			log.error("message : {}", url);
			throw new DomainValidationException();
		}
	}
}
