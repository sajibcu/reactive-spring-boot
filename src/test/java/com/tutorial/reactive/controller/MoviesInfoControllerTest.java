package com.tutorial.reactive.controller;

import com.tutorial.reactive.model.MovieInfo;
import com.tutorial.reactive.service.MovieInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;


@WebFluxTest( controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
class MoviesInfoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private MovieInfoService movieInfoServiceMock;

    private String MOVIE_URL = "/movieInfos";

    @Test
    void add() {
        //given
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(movieInfoServiceMock.add(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Batman Begins1",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );

        //when
        webTestClient
                .post()
                .uri(MOVIE_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {

                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedMovieInfo!=null;
                    assert savedMovieInfo.getMovieInfoId()!=null;
                    assertEquals("mockId",savedMovieInfo.getMovieInfoId());
                });


        //then
    }

    @Test
    void getAll() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));


        when( movieInfoServiceMock.getAll()).thenReturn(Flux.fromIterable(movieinfos));

        webTestClient
                .get()
                .uri( MOVIE_URL )
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getAllById() {

        var movieId = "abc";

        var movieinfo = new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when( movieInfoServiceMock.getAllById( isA(String.class) ))
                .thenReturn(Mono.just(movieinfo));

        webTestClient
                .get()
                .uri( MOVIE_URL + "/{id}", movieId )
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody( MovieInfo.class )
                .consumeWith( movieInfoEntityExchangeResult -> {
                    var movieInfo1 = movieInfoEntityExchangeResult.getResponseBody();
                    assert movieInfo1 != null;
                    assert movieInfo1.getName() != null;
                    assertEquals("Dark Knight Rises", movieInfo1.getName());
                });
    }

    @Test
    void deleteMovieInfoById() {
        var movieId = "abc";
        when(movieInfoServiceMock.deleteByMovieId(isA(String.class)))
                .thenReturn(Mono.empty());
        webTestClient
                .delete()
                .uri(MOVIE_URL+"/{id}", movieId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}