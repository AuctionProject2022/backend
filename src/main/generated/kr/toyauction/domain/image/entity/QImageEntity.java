package kr.toyauction.domain.image.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QImageEntity is a Querydsl query type for ImageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImageEntity extends EntityPathBase<ImageEntity> {

    private static final long serialVersionUID = -2130629394L;

    public static final QImageEntity imageEntity = new QImageEntity("imageEntity");

    public final kr.toyauction.global.entity.QBaseEntity _super = new kr.toyauction.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDatetime = _super.createDatetime;

    //inherited
    public final BooleanPath enabled = _super.enabled;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath path = createString("path");

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public final EnumPath<ImageType> type = createEnum("type", ImageType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDatetime = _super.updateDatetime;

    public QImageEntity(String variable) {
        super(ImageEntity.class, forVariable(variable));
    }

    public QImageEntity(Path<? extends ImageEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImageEntity(PathMetadata metadata) {
        super(ImageEntity.class, metadata);
    }

}

