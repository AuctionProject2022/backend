package kr.toyauction.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 925595865L;

    public static final QMember member = new QMember("member1");

    public final kr.toyauction.global.entity.QBaseEntity _super = new kr.toyauction.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDatetime = _super.createDatetime;

    //inherited
    public final BooleanPath enabled = _super.enabled;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath picture = createString("picture");

    public final EnumPath<Platform> platform = createEnum("platform", Platform.class);

    public final StringPath platformId = createString("platformId");

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDatetime = _super.updateDatetime;

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

