package com.demo.M2M_Actor_Movie.mapping;

import com.demo.M2M_Actor_Movie.dto.ActorRequest;
import com.demo.M2M_Actor_Movie.dto.MovieRequest;
import com.demo.M2M_Actor_Movie.dto.MovieResponse;
import com.demo.M2M_Actor_Movie.entity.Actor;
import com.demo.M2M_Actor_Movie.entity.Movie;
import com.demo.M2M_Actor_Movie.enums.MovieGenre;
import com.demo.M2M_Actor_Movie.enums.MovieStatus;
import com.demo.M2M_Actor_Movie.exception.CustomException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);
    @Mapping(source = "movieTitle",target = "movieTitle")
    @Mapping(source = "movieDescription",target = "movieDescription")
    @Mapping(source = "movieBudget",target = "movieBudget")
    @Mapping(source = "movieProducer",target = "movieProducer")
    @Mapping(source = "movieDirector",target = "movieDirector")
    @Mapping(source = "movieReleaseDate",target = "movieReleaseDate")
    @Mapping(source = "movieGenre", target = "movieGenre", qualifiedByName = "mapMovieGenre")
    @Mapping(source = "movieStatus", target = "movieStatus", qualifiedByName = "mapMovieStatus")
    Movie movieRequestToMovieEntityMapping(MovieRequest movieRequest);

    @Mapping(source = "movieTitle",target = "movieTitle")
    @Mapping(source = "movieDescription",target = "movieDescription")
    @Mapping(source = "movieBudget",target = "movieBudget")
    @Mapping(source = "movieProducer",target = "movieProducer")
    @Mapping(source = "movieDirector",target = "movieDirector")
    @Mapping(source = "movieReleaseDate",target = "movieReleaseDate")
    MovieResponse movieEntityTOMovieResponseMapping(Movie movie);

    @Named("mapMovieGenre")
    static MovieGenre mapMovieGenre(String movieGenre) {
        try {
            return MovieGenre.valueOf(movieGenre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid movie status: " + movieGenre);
        }
    }
    @Named("mapMovieStatus")
    static MovieStatus mapMovieStatus(String movieStatus) {
        try {
            return MovieStatus.valueOf(movieStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid movie status: " + movieStatus);
        }
    }
}
