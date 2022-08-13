package kr.toyauction.domain.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.toyauction.domain.product.dto.ProductGetRequest;
import kr.toyauction.global.repository.QuerySupport;
import org.springframework.lang.NonNull;

public class ProductQuerySupport extends QuerySupport {

    protected BooleanBuilder where(@NonNull final ProductGetRequest productGetRequest) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.orAllOf(new BooleanExpression[]{

//                productIdEq(productGetRequest.getId()),
//                productUsernameContains(productGetRequest.getUsername())
        });
        return booleanBuilder;
    }
}
