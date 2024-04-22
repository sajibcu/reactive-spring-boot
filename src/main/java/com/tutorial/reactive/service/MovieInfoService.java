package com.tutorial.reactive.service;

import com.tutorial.reactive.model.MovieInfo;
import com.tutorial.reactive.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MovieInfoService {
    @Autowired
    private MovieInfoRepository movieInfoRepository;

    public Mono<MovieInfo> add(MovieInfo movieInfo) {
        return movieInfoRepository.save( movieInfo );
    }

    public Flux<MovieInfo> getAll() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getAllById(String id) {
        return movieInfoRepository.findById( id );
    }

    public Flux<MovieInfo> getAllByYear(Integer year) {
        return movieInfoRepository.findAllByYear( year);
    }

    public Mono<Void> deleteByMovieId(String id) {
        return movieInfoRepository.deleteById(id);
    }
}
