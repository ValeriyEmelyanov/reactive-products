package com.example.reactiveproducts.service.impl;

import com.example.reactiveproducts.model.Product;
import com.example.reactiveproducts.repo.ProductRepository;
import com.example.reactiveproducts.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Flux<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> getById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> create(Mono<Product> productMono) {
        return productMono.flatMap(productRepository::save);
    }

    @Override
    public Mono<Product> update(long id, Mono<Product> productMono) {
        return productRepository.findById(id)
                .flatMap(p ->
                        productMono.map(m -> {
                            p.setTitle(m.getTitle());
                            return p;
                        }))
                .flatMap(productRepository::save);
    }

    @Override
    public Mono<Void> delete(long id) {
        return productRepository.deleteById(id);
    }
}
