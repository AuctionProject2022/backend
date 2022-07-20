package kr.toyauction.domain.image.dto;

import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.entity.ImageType;
import kr.toyauction.global.dto.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagePostResponse extends BaseResponse {

	private Long imageId;
	private Long memberId;
	private ImageType type;
	private Long targetId;
	private String path;

	public ImagePostResponse(final ImageEntity imageEntity, final String imageHost) {
		this.imageId = imageEntity.getId();
		this.memberId = imageEntity.getMemberId();
		this.type = imageEntity.getType();
		this.targetId = imageEntity.getTargetId();
		this.path = imageHost + imageEntity.getPath();
		this.createDatetime = imageEntity.getCreateDatetime();
		this.updateDatetime = imageEntity.getUpdateDatetime();
	}
}
