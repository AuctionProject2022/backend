package kr.toyauction.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.toyauction.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
