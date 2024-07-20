package com.demo.M2M_Actor_Movie.service;

import com.demo.M2M_Actor_Movie.dto.*;
import com.demo.M2M_Actor_Movie.entity.Actor;
import com.demo.M2M_Actor_Movie.entity.ActorMovieJoin;
import com.demo.M2M_Actor_Movie.entity.Movie;
import com.demo.M2M_Actor_Movie.exception.CustomException;
import com.demo.M2M_Actor_Movie.mapping.ActorMapper;
import com.demo.M2M_Actor_Movie.mapping.MovieMapper;
import com.demo.M2M_Actor_Movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    public String saveMovieInfo(MovieRequest mvi) {
        if(mvi != null){
            Movie movie = MovieMapper.INSTANCE.movieRequestToMovieEntityMapping(mvi);
            if(mvi.getActorMovieJoinRequestList().size() !=0){
                List<ActorMovieJoin> actorMovieJoinList = new ArrayList<>();
                for(ActorMovieJoinRequest actorMovieRequestJoin : mvi.getActorMovieJoinRequestList()){
                    ActorMovieJoin actorMovieJoin = new ActorMovieJoin();
                    Actor actor = ActorMapper.INSTANCE.actorRequestToActorEntityMapping(actorMovieRequestJoin.getActorRequest());
                    actorMovieJoin.setMovie(movie);
                    actorMovieJoin.setActor(actor);
                    actorMovieJoinList.add(actorMovieJoin);
                }
                movie.setActorMovieJoinList(actorMovieJoinList);
            }
            movieRepository.save(movie);
            return  "Movie Recrod Saved Succesfully..";
        }else
            return "Please Enter Movie Data";
    }
    public Object getAllMovies() {
        Optional<List<Movie>> optionalMoviesList = Optional.ofNullable(movieRepository.findAll());
        if(optionalMoviesList.isPresent()){
            List<Movie> movieListFromDB = optionalMoviesList.get();
            List<MovieResponse> movieResponsesList = new ArrayList<>();
            for(Movie movie  : movieListFromDB){
             MovieResponse movieResponse = MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(movie);
             List<ActorMovieJoinResponse> actorMovieJoinResponseList = new ArrayList<>();
             for(ActorMovieJoin actorMovieJ : movie.getActorMovieJoinList()){
                ActorMovieJoinResponse actorMovieJoinResponse = new ActorMovieJoinResponse();
                actorMovieJoinResponse.setActorResponse(ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actorMovieJ.getActor()));
                actorMovieJoinResponseList.add(actorMovieJoinResponse);
             }
             movieResponse.setActorMovieJoinResponseList(actorMovieJoinResponseList);
             movieResponsesList.add(movieResponse);
            }
            return  movieResponsesList;
        }
        else
            throw new CustomException("No Recrod Found...");
    }
    public Object deleteById(Integer movieId){
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if(optionalMovie.isPresent()){
            movieRepository.deleteById(movieId);
            return  "Movie Record Deleted Succesfully...";
        }
        else
            return  "No Movie Found For Given Id..";
    }
    public String updateMovie(MovieRequest movieRequest, int movieId) {

        Optional<Movie> movieOptional = movieRepository.findById(movieId);
        if(movieOptional.isPresent()){
            Movie movie = movieOptional.get();
            if(!movieRequest.getMovieTitle().isEmpty() && movieRequest.getMovieTitle() != null)
                movie.setMovieTitle(movieRequest.getMovieTitle());
            if(!movieRequest.getMovieDescription().isEmpty() && movieRequest.getMovieDescription() != null)
                movie.setMovieDescription(movieRequest.getMovieDescription());
            if(!movieRequest.getMovieProducer().isEmpty() && movieRequest.getMovieProducer() != null)
                movie.setMovieProducer(movieRequest.getMovieProducer());
            if(!movieRequest.getMovieDirector().isEmpty() && movieRequest.getMovieDirector() != null)
                movie.setMovieDescription(movieRequest.getMovieDirector());
            movie.setMovieReleaseDate(movieRequest.getMovieReleaseDate());
            if(movieRequest.getMovieBudget() >= 20000000)
                movie.setMovieDescription(movieRequest.getMovieDescription());
            List<Actor> alreayExistingActors = movie.getActorMovieJoinList().stream().map(st->{return st.getActor();}).collect(Collectors.toList());
            List<Actor> requestActors = movieRequest.getActorMovieJoinRequestList().stream().map(st->{return ActorMapper.INSTANCE.actorRequestToActorEntityMapping(st.getActorRequest());}).collect(Collectors.toList());
            int min = Math.min(alreayExistingActors.size(),requestActors.size());
            for(int i=0;i<min;i++){
            Actor alreayPresentActor = alreayExistingActors.get(i);
            Actor requestedActor = requestActors.get(i);
            if(!requestedActor.getActorName().isEmpty() && requestedActor.getActorName() != null)
                alreayPresentActor.setActorName(requestedActor.getActorName());
            if(!requestedActor.getActorMailId().isEmpty() && requestedActor.getActorMailId() != null)
                alreayPresentActor.setActorMailId(requestedActor.getActorMailId());
            if(!requestedActor.getActorAddress().isEmpty() && requestedActor.getActorAddress() != null)
                alreayPresentActor.setActorAddress(requestedActor.getActorAddress());
            if(!requestedActor.getActorMobileNo().isEmpty() && requestedActor.getActorMobileNo() != null)
                alreayPresentActor.setActorMobileNo(requestedActor.getActorMobileNo());
            if(!requestedActor.getActorGender().equals("") && requestedActor.getActorGender() != null)
                alreayPresentActor.setActorGender(requestedActor.getActorGender());
            if(requestedActor.getActorAge() > 0 )
                alreayPresentActor.setActorAge(requestedActor.getActorAge());
            }
            for(int k = min;k<requestActors.size();k++){
                Actor newMovie = requestActors.get(k);
                ActorMovieJoin newActorMovieJoin = new ActorMovieJoin();
                newActorMovieJoin.setActor(newMovie);
                newActorMovieJoin.setMovie(movie);
                movie.getActorMovieJoinList().add(newActorMovieJoin);
            }
            movieRepository.save(movie);
            return "Movie Updated Succesfully...";
        }else
            return "No Record Found For This ID..";
    }
    public Object getMovieDataBasedOnSearch(MovieSearch movieSearch){
        List<Movie> movieList = movieRepository.findAll(movieRepository.findByCriteria(movieSearch)).stream().collect(Collectors.toList());
        if(movieList.isEmpty()){
            throw new CustomException("No records found for the given search criteria");
        }
        List<MovieResponse> movieResponsesList = new ArrayList<>();
        for(Movie movie : movieList){
            MovieResponse movieResponse = MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(movie);
            List<ActorMovieJoinResponse> actorMovieJoinResponseList = new ArrayList<>();
            for(ActorMovieJoin actorMovieJoin : movie.getActorMovieJoinList()){
                ActorMovieJoinResponse actorMovieJoinResponse = new ActorMovieJoinResponse();

                if(movieSearch.getMovieTitle() != null && !movieSearch.getMovieTitle().equals("")){
                    if(movieSearch.getMovieTitle().equals(actorMovieJoin.getMovie().getMovieTitle())){
                        actorMovieJoinResponse.setActorResponse(ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actorMovieJoin.getActor()));
                    }
                }
                if(movieSearch.getMovieDirector() != null && !movieSearch.getMovieDirector().equals("")){
                    if(movieSearch.getMovieDirector().equals(actorMovieJoin.getMovie().getMovieDirector())){
                        actorMovieJoinResponse.setActorResponse(ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actorMovieJoin.getActor()));
                    }
                }
                if(movieSearch.getMovieProducer() != null && !movieSearch.getMovieProducer().equals("")){
                    if(movieSearch.getMovieProducer().equals(actorMovieJoin.getMovie().getMovieProducer())){
                        actorMovieJoinResponse.setActorResponse(ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actorMovieJoin.getActor()));
                    }
                }
                if(movieSearch.getActorGender() != null && !movieSearch.getActorGender().equals("")){
                    if(actorMovieJoin.getActor().getActorGender().equals(movieSearch.getActorGender()))
                        actorMovieJoinResponse.setActorResponse(ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actorMovieJoin.getActor()));
                }
                if(movieSearch.getActorName() != null && !movieSearch.getActorName().equals("")){
                    if(movieSearch.getActorName().equals(actorMovieJoin.getActor().getActorName()))
                        actorMovieJoinResponse.setActorResponse(ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actorMovieJoin.getActor()));
                }
                actorMovieJoinResponseList.add(actorMovieJoinResponse);
            }
            movieResponse.setActorMovieJoinResponseList(actorMovieJoinResponseList);
            movieResponsesList.add(movieResponse);
        }
        return movieResponsesList;
    }
}
