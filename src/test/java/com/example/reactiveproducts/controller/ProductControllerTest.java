package com.example.reactiveproducts.controller;

import com.example.reactiveproducts.model.Product;
import com.example.reactiveproducts.repo.ProductRepository;
import com.example.reactiveproducts.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductController.class)
@Import(ProductServiceImpl.class)
class ProductControllerTest {

    @MockBean
    ProductRepository productRepository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getAll() {
        Product product1 = new Product(1L, "Product 1");
        Product product2 = new Product(2L, "Product 2");
        List<Product> products = List.of(product1, product2);
        Flux<Product> productFlux = Flux.fromIterable(products);
        PageRequest pageRequest = PageRequest.of(0, 10).withSort(Sort.by("id"));

        Mockito
                .when(productRepository.findAllBy(pageRequest))
                .thenReturn(productFlux);
        Mockito
                .when(productRepository.count())
                .thenReturn(Mono.just(2L));

        webClient.get().uri("/products?page=0&size=10")
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(productRepository, Mockito.times(1)).findAllBy(pageRequest);
        Mockito.verify(productRepository, Mockito.times(1)).count();
    }

    @Test
    void getById() {
        long productId = 1L;
        String productTitle = "Product 1";
        Product product = new Product(productId, productTitle);

        Mockito
                .when(productRepository.findById(productId))
                .thenReturn(Mono.just(product));

        webClient.get().uri("/products/{id}", productId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(productId)
                .jsonPath("$.title").isEqualTo(productTitle);

        Mockito.verify(productRepository, Mockito.times(1)).findById(productId);
    }

    @Test
    void getByIdNotFound() {
        long productId = 1000L;

        Mockito
                .when(productRepository.findById(productId))
                .thenReturn(Mono.empty());

        webClient.get().uri("/products/{id}", productId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();

        Mockito.verify(productRepository, Mockito.times(1)).findById(productId);
    }

    @Test
    void create() {
        long productId = 1L;
        String productTitle = "Product 1";
        Product product = new Product();
        product.setTitle(productTitle);
        Product saved = new Product(productId, productTitle);

        Mockito
                .when(productRepository.save(product))
                .thenReturn(Mono.just(saved));

        webClient.post().uri("/products").bodyValue(product)
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    void update() {
        long productId = 1L;
        String productTitle = "Product 1";
        Product product = new Product(productId, productTitle);
        String updatedProductTitle = "Product 1 updated";
        Product updated = new Product(productId, updatedProductTitle);

        Mockito
                .when(productRepository.findById(productId))
                .thenReturn(Mono.just(product));
        Mockito
                .when(productRepository.save(product))
                .thenReturn(Mono.just(updated));

        webClient.put().uri("/products/{id}", productId).bodyValue(updated)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(productRepository, Mockito.times(1)).save(updated);
    }

    @Test
    void delete() {
        long productId = 1L;

        Mockito
                .when(productRepository.deleteById(productId))
                .thenReturn(Mono.empty());

        webClient.delete().uri("/products/{id}", productId)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(productId);
    }
}