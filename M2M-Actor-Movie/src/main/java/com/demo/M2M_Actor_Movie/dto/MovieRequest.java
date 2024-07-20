package com.demo.M2M_Actor_Movie.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.sql.Date;
import java.util.List;
@Data
public class MovieRequest {
    @NotEmpty(message = "Movie Title is mandatory")
  //  @Size(min = 10, max = 100, message = "Movie title must be between 10 and 100 characters")
    private String movieTitle;
    @NotEmpty(message = "Movie description is mandatory")
    @Size(max = 1000, message = "Movie description must be at most 1000 characters")
    private String movieDescription;
    @NotNull(message = "Movie Budget mandatory")
    @Positive(message = "Budget must be +ve")
    @Min(value = 10000000, message = "Budget must be at least 10,000,000")
    private Integer movieBudget;
    @NotNull(message = "Movie genre is mandatory")
    private String movieGenre;
    @NotNull(message = "Movie status is mandatory")
    private String movieStatus;
    @NotNull(message = "Movie Producer mandatory")
    @Pattern(regexp = "^[A-Za-z]+\\s[A-Za-z]+$", message = "Producer name must be in the format 'firstname lastname'")
    private String movieProducer;
    @NotNull(message = "Movie Director mandatory")
    @Pattern(regexp = "^[A-Za-z]+\\s[A-Za-z]+$", message = "Director name must be in the format 'firstname lastname'")
    private String movieDirector;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
    private Date movieReleaseDate;
    private List<ActorMovieJoinRequest> actorMovieJoinRequestList;
}
