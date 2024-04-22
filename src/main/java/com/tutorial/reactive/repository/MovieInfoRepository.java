package com.tutorial.reactive.repository;

import com.tutorial.reactive.model.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

    Flux<MovieInfo> findAllByYear(Integer year);

    Mono<MovieInfo> findFirstByName(String name);
}
