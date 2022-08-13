package kr.toyauction.domain.product.repository;

import kr.toyauction.domain.member.dto.MemberGetRequest;
import kr.toyauction.domain.member.entity.Member;
import kr.toyauction.domain.product.dto.ProductGetRequest;
import kr.toyauction.domain.product.dto.ProductGetResponse;
import kr.toyauction.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface ProductQueryRepository {
    Page<ProductGetResponse> page(@NonNull final Pageable pageable, final ProductGetRequest productGetRequest);

}
