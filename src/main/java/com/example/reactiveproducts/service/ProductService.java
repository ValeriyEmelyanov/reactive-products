package com.example.reactiveproducts.service;

import com.example.reactiveproducts.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<Page<Product>> getPage(PageRequest pageRequest);

    Mono<Product> getById(long id);

    Mono<Product> create(Mono<Product> productMono);

    Mono<Product> update(long id, Mono<Product> productMono);

    Mono<Void> delete(long id);
}
