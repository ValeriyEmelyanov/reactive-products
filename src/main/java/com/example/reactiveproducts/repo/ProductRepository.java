package com.example.reactiveproducts.repo;

import com.example.reactiveproducts.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ProductRepository extends ReactiveSortingRepository<Product, Long> {
    Flux<Product> findAllBy(Pageable pageable);
}
