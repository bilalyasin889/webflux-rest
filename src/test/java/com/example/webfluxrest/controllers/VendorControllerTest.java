package com.example.webfluxrest.controllers;

import com.example.webfluxrest.domain.Vendor;
import com.example.webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository repository;
    VendorController controller;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(VendorRepository.class);
        controller = new VendorController(repository);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void list() {
        BDDMockito.given(repository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Fred").lastName("Flintstone").build(),
                        Vendor.builder().firstName("Barney").lastName("Rubble").build()));

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        BDDMockito.given(repository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").lastName("Johns").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/someid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        BDDMockito.given(repository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("Jessie").lastName("Pinkman").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void update() {
        BDDMockito.given(repository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().build());

        webTestClient.put()
                .uri("/api/v1/vendors/someid")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
