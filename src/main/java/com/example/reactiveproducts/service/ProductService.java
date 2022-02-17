package com.example.reactiveproducts.service;

import com.example.reactiveproducts.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> getAll();
    Mono<Product> getById(long id);
    Mono<Product> create(Mono<Product> productMono);
    Mono<Product> update(long id, Mono<Product> productMono);
    Mono<Void> delete(long id);
}
