package kr.toyauction.domain.image.entity;

import kr.toyauction.global.entity.BaseEntity;
import kr.toyauction.global.entity.EntitySupport;
import kr.toyauction.global.exception.DomainValidationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "image")
public class ImageEntity extends BaseEntity implements EntitySupport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	@Enumerated(EnumType.STRING)
	private ImageType type;

	private Long targetId;

	private String path;

	@Override
	public void validation() {

		if (memberId == null) {
			log.error("memberId is null");
			throw new DomainValidationException();
		}

		if (!StringUtils.hasText(path)) {
			log.error("path : {}", path);
			throw new DomainValidationException();
		}
	}
}
