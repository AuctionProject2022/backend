package kr.toyauction.domain.product.repository;

import kr.toyauction.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop10ByProductNameContainsOrderByIdDesc(String productName);
}
