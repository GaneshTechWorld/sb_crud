package com.demo.M2M_Actor_Movie.dto;
import lombok.Data;
@Data
public class ActorMovieJoinRequest {
    private MovieRequest movieRequest;
    private ActorRequest actorRequest;
}
