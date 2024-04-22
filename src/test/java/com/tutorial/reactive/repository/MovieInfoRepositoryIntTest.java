package com.tutorial.reactive.repository;

import com.tutorial.reactive.model.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

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

    @Test
    void findAll() {

        var allMovieInfo = movieInfoRepository.findAll();
        StepVerifier.create( allMovieInfo )
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    void findOneById() {

        var movieInfoMono = movieInfoRepository.findById( "abc").log();

        StepVerifier.create( movieInfoMono )
                .assertNext( movieInfo -> {
                    assertEquals ( "Dark Knight Rises" , movieInfo.getName() );
                } )
                .verifyComplete();

    }

    @Test
    void save() {

        var movieInfo = new MovieInfo(null, "Aynabaji",
                2019, List.of("Chanchal Chowdhury", "Masuma Rahman Nabila"), LocalDate.parse("2019-06-15"));

        StepVerifier.create( movieInfoRepository.save( movieInfo ) )
                .assertNext( movieInfo1 -> {
                    assertNotNull( movieInfo1.getMovieInfoId() );
                    assertEquals ( "Aynabaji" , movieInfo.getName() );
                } )
                .verifyComplete();

    }

    @Test
    void deletedById() {
        movieInfoRepository.deleteById("abc").block();
        var allMovieInfo = movieInfoRepository.findAll().log();
        StepVerifier.create( allMovieInfo )
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void findAllByYear() {
        var movies = movieInfoRepository.findAllByYear(2005);
        StepVerifier.create( movies )
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findFirstByName() {
        var movieName = "Batman Begins";
        var movie = movieInfoRepository.findFirstByName(movieName);

        StepVerifier.create( movie )
                .assertNext( movieInfo -> {
                    assertEquals(movieName, movieInfo.getName());
                })
                .verifyComplete();
    }
}