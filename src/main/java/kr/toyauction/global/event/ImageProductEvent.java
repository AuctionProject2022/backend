package kr.toyauction.global.event;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageProductEvent {

	private Long thumbnailImageId;
	private Collection<Long> imageIds;
	private Long targetId;
}
