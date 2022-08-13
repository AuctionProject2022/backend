package kr.toyauction.global.event;

import kr.toyauction.domain.image.entity.ImageType;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageRegistryEvent {

	private Collection<Long> imageIds;
	private ImageType imageType;
	private Long targetId;
}
