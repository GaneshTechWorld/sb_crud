package com.demo.M2M_Actor_Movie.dto;

import com.demo.M2M_Actor_Movie.enums.ActorRole;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;
@Data
public class ActorRequest {
    @NotBlank(message = "Actor name is mandatory")
    @Pattern(regexp = "^[A-Za-z]+\\s[A-Za-z]+$", message = "Actor name must be in the format 'firstname lastname'")
    private String actorName;
    @NotNull(message = "Actor age is mandatory")
    @Positive(message = "Actor age must be positive")
    @Min(value = 18, message = "Age must be not less than 18")
    @Max(value = 90, message = "Age must be greater than or equal to 90")
    private Double actorAge;
    @NotBlank(message = "Actor gender is mandatory")
    //@Pattern(regexp = "(?i)Male|Female|Other", message = "Gender must be either Male, Female, or Other") //it will accept like Male,male,MALE etc
    private String actorGender;
    @NotBlank(message = "Actor role is mandatory")
      private String actorRole;

    @Size(max = 255, message = "Address must be less than or equal to 255 characters")
    private String actorAddress;
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be null or a 10-digit number")
    private String actorMobileNo;
    @Email(message = "Invalid email address")
    private String actorMailId;
    private List<ActorMovieJoinRequest> actorMovieJoinRequestList;
}
