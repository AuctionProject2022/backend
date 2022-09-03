package kr.toyauction.domain.image.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.toyauction.domain.image.entity.ImageEntity;
import kr.toyauction.domain.image.entity.ImageType;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
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
