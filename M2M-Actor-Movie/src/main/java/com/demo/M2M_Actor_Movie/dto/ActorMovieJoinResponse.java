package com.demo.M2M_Actor_Movie.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActorMovieJoinResponse {
    private MovieResponse movieResponse;
    private ActorResponse actorResponse;
}
