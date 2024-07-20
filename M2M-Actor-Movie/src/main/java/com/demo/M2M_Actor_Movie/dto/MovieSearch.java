package com.demo.M2M_Actor_Movie.dto;

import lombok.Data;
@Data
public class MovieSearch {
    private String movieTitle;
    private String movieProducer;
    private String movieDirector;
    private String actorName;
    private String actorGender;
}
