package com.tutorial.reactive.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void integerFlux() {
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    void integerFlux_approach2() {
        var flux = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class )
                .getResponseBody();
        StepVerifier.create( flux )
                .expectNext(1,3,4,6)
                .verifyComplete();
    }

    @Test
    void mono() {
        webTestClient.get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith( stringResponse -> {
                    var responseBody = stringResponse.getResponseBody();
                    assertEquals("Hello Sajib", responseBody);
                } );
    }

    @Test
    void stream() {
        var flux = webTestClient.get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class )
                .getResponseBody();
        StepVerifier.create( flux )
                .expectNext(0L,1L,2L,3L,4L)
                .thenCancel()
                .verify();
    }
}