package com.tutorial.reactive.controller;

import com.tutorial.reactive.model.MovieInfo;
import com.tutorial.reactive.service.MovieInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movieInfos")
public class MoviesInfoController {

    @Autowired
    private MovieInfoService movieInfoService;

    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    Mono<MovieInfo> add (@RequestBody MovieInfo movieInfo ) {
        return movieInfoService.add( movieInfo );
    }

    @GetMapping
    Flux<MovieInfo> getAll(
            @RequestParam(value = "year", required = false) Integer year
    ) {
        if( year != null )
            return movieInfoService.getAllByYear(year);
        return movieInfoService.getAll();

    }

    @GetMapping("/{id}")
    Mono<MovieInfo> getAllById( @PathVariable String id) {
        return movieInfoService.getAllById( id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    Mono<Void> deleteByMovieId( @PathVariable String id) {
        return movieInfoService.deleteByMovieId( id);
    }
}
