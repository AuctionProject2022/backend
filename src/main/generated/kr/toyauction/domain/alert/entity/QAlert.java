package kr.toyauction.domain.alert.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlert is a Querydsl query type for Alert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlert extends EntityPathBase<Alert> {

    private static final long serialVersionUID = 1872687819L;

    public static final QAlert alert = new QAlert("alert");

    public final kr.toyauction.global.entity.QBaseEntity _super = new kr.toyauction.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDatetime = _super.createDatetime;

    //inherited
    public final BooleanPath enabled = _super.enabled;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDatetime = _super.updateDatetime;

    public QAlert(String variable) {
        super(Alert.class, forVariable(variable));
    }

    public QAlert(Path<? extends Alert> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlert(PathMetadata metadata) {
        super(Alert.class, metadata);
    }

}

