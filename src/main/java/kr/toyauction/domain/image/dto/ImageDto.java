package kr.toyauction.domain.image.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.entity.ImageType;
import lombok.*;

import java.awt.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private Long imageId;
    private String imageUrl;
    private ImageType imageType;

    public ImageDto(ImageEntity image){
        this.imageId = image.getId();
        this.imageUrl = image.getPath();
        this.imageType = image.getType();
    }
}
