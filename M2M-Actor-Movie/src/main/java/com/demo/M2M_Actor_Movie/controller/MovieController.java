package com.demo.M2M_Actor_Movie.controller;

import com.demo.M2M_Actor_Movie.dto.MovieRequest;
import com.demo.M2M_Actor_Movie.dto.MovieSearch;
import com.demo.M2M_Actor_Movie.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class MovieController {
    @Autowired
    MovieService movieService;
    @PostMapping(value = "saveMovieInfo", consumes = "application/json")
    public ResponseEntity<Object> saveMovie(@Valid @RequestBody MovieRequest movie, BindingResult bindingResult) {
        System.out.println("Received Actor data: " + movie);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessageBuilder = new StringBuilder("Validation failed:\n");
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errorMessageBuilder.append(fieldName).append(": ").append(errorMessage).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessageBuilder.toString());
        }
        return new ResponseEntity<> (movieService.saveMovieInfo(movie),HttpStatus.OK);
    }
    @GetMapping("getAllMovie")
    public ResponseEntity<Object> getAllMoviesDetails(){
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatusCode.valueOf(200));
    }
    @DeleteMapping("deleteMovieById")
    public ResponseEntity<Object> deleteActorById(@RequestParam Integer movieId){
     String status;
        if(!movieId.equals("")  || movieId != null)
            return new ResponseEntity<>(movieService.deleteById(movieId), HttpStatus.OK);
        return new ResponseEntity<>("Please Enter Proper Id..", HttpStatus.BAD_REQUEST);
    }
    @PutMapping("updateMovie")
    public ResponseEntity<String> updateMovie(@RequestBody MovieRequest movieRequest,@RequestParam final int movieId){
        if(movieRequest != null &&  movieId >0)
            return new ResponseEntity<>( movieService.updateMovie(movieRequest,movieId),HttpStatus.OK);
     return new ResponseEntity<>("Record Not Updated..",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("movieSearch")
    public ResponseEntity<Object> getMovieBasedOnSearch(@RequestBody MovieSearch movieSearch){
        if(movieSearch !=null){
            return new ResponseEntity<>( movieService.getMovieDataBasedOnSearch(movieSearch),HttpStatus.OK);
        }
        return new ResponseEntity<>("Record Not Updated..",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

