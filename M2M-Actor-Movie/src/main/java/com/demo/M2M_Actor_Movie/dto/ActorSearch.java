package com.demo.M2M_Actor_Movie.dto;

import lombok.Data;

@Data
public class ActorSearch {
    private String actorName;
    private String actorGender;
    private String movieTitle;
    private String movieProducer;
    private String movieDirector;
}
