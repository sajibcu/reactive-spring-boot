package com.tutorial.reactive.controller;

import com.tutorial.reactive.model.MovieInfo;
import com.tutorial.reactive.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoControllerIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    private String MOVIE_URL = "/movieInfos";

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
        movieInfoRepository.saveAll( movieinfos )
                .blockLast();
    }

    @AfterEach
    void tearDown(){
        movieInfoRepository.deleteAll();
    }
    @Test
    void add() {

        var movieInfo = new MovieInfo(null, "Aynabaji",
                2019, List.of("Chanchal Chowdhury", "Masuma Rahman Nabila"), LocalDate.parse("2019-06-15"));


        webTestClient
                .post()
                .uri( MOVIE_URL )
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith( movieInfoEntityExchangeResult -> {
                    var expected = movieInfoEntityExchangeResult.getResponseBody();

                    assert expected !=null;
                    assert expected.getMovieInfoId() !=null;
                   Assertions.assertEquals ( "Aynabaji" , expected.getName() );
                });
    }

    @Test
    void getAll() {
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
    void getAllByYear() {

        var uri = UriComponentsBuilder.fromUriString( MOVIE_URL)
                        .queryParam("year", 2005)
                                .buildAndExpand()
                                        .toUri();
        webTestClient
                .get()
                .uri( uri )
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
    }

    @Test
    void getAllById() {
        var movieId = "abc";
        webTestClient
                .get()
                .uri( MOVIE_URL + "/{id}", movieId )
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
    }

    @Test
    void deleteByMovieId() {
        var movieId = "abc";
        webTestClient
                .delete()
                .uri( MOVIE_URL + "{id}", movieId)
                .exchange()
                .expectStatus()
                .isNoContent();

    }
}