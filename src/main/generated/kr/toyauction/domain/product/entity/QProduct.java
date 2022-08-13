package kr.toyauction.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = -626198357L;

    public static final QProduct product = new QProduct("product");

    public final kr.toyauction.global.entity.QBaseEntity _super = new kr.toyauction.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDatetime = _super.createDatetime;

    public final EnumPath<DeliveryOption> deliveryOption = createEnum("deliveryOption", DeliveryOption.class);

    public final StringPath detail = createString("detail");

    //inherited
    public final BooleanPath enabled = _super.enabled;

    public final DateTimePath<java.time.LocalDateTime> endSaleDateTime = createDateTime("endSaleDateTime", java.time.LocalDateTime.class);

    public final EnumPath<ExchangeType> exchangeType = createEnum("exchangeType", ExchangeType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> minBidPrice = createNumber("minBidPrice", Integer.class);

    public final EnumPath<ProductCondition> productCondition = createEnum("productCondition", ProductCondition.class);

    public final StringPath productName = createString("productName");

    public final EnumPath<ProductSttus> productSttus = createEnum("productSttus", ProductSttus.class);

    public final EnumPath<PurchaseTime> purchaseTime = createEnum("purchaseTime", PurchaseTime.class);

    public final NumberPath<Long> registerMemberId = createNumber("registerMemberId", Long.class);

    public final NumberPath<Integer> rightPrice = createNumber("rightPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startSaleDateTime = createDateTime("startSaleDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> unitPrice = createNumber("unitPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDatetime = _super.updateDatetime;

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

