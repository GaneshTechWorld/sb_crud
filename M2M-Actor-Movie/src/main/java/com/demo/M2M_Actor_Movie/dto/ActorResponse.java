package com.demo.M2M_Actor_Movie.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActorResponse {
    private String actorName;
    private Double actorAge;
    private String actorGender;
    private String actorAddress;
    private String actorMobileNo;
    private String actorMailId;
    private List<ActorMovieJoinResponse> actorMovieJoinResponseList;
}
