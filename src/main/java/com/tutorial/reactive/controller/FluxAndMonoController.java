package com.tutorial.reactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> integerFlux() {
        return Flux.just(1,3,4,6)
                .delayElements( Duration.ofSeconds(1));
    }

    @GetMapping("/mono")
    public Mono<String> mono() {
        return Mono.just( "Hello Sajib" );
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE )
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(1)).log();
    }
}
