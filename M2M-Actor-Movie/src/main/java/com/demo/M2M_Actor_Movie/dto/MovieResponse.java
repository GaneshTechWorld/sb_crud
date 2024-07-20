package com.demo.M2M_Actor_Movie.dto;

import com.demo.M2M_Actor_Movie.entity.ActorMovieJoin;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;


@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieResponse {
    private String movieTitle;
    private String movieDescription;
    private int movieBudget;
    private String movieProducer;
    private String movieDirector;
    private Date movieReleaseDate;
    private List<ActorMovieJoinResponse> actorMovieJoinResponseList;
}
