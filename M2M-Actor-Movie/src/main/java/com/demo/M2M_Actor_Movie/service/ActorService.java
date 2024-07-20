package com.demo.M2M_Actor_Movie.service;

import com.demo.M2M_Actor_Movie.dto.*;
import com.demo.M2M_Actor_Movie.entity.Actor;
import com.demo.M2M_Actor_Movie.entity.ActorMovieJoin;
import com.demo.M2M_Actor_Movie.entity.Movie;
import com.demo.M2M_Actor_Movie.enums.ActorGender;
import com.demo.M2M_Actor_Movie.exception.CustomException;
import com.demo.M2M_Actor_Movie.mapping.ActorMapper;
import com.demo.M2M_Actor_Movie.mapping.MovieMapper;
import com.demo.M2M_Actor_Movie.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActorService {
    @Autowired
    ActorRepository actorRepository;
  @Transactional
    public void saveActorInfo(ActorRequest actorRequest){
        Actor actor = ActorMapper.INSTANCE.actorRequestToActorEntityMapping(actorRequest);
        if(actorRequest.getActorMovieJoinRequestList() != null && actorRequest.getActorMovieJoinRequestList().size() != 0){
            List<ActorMovieJoin> actorMovieJoinList = new ArrayList<>();
            for(ActorMovieJoinRequest actorMovieJoinRequest : actorRequest.getActorMovieJoinRequestList()){
                ActorMovieJoin actorMovieJoin = new ActorMovieJoin();
                MovieRequest mRequrst = actorMovieJoinRequest.getMovieRequest();
                Movie movie = MovieMapper.INSTANCE.movieRequestToMovieEntityMapping(mRequrst);
                actorMovieJoin.setMovie(movie);
                actorMovieJoin.setActor(actor);
                actorMovieJoinList.add(actorMovieJoin);
            }
            actor.setActorMovieJoinList(actorMovieJoinList);
        }
        actorRepository.save(actor);
    }

    public Object getAllActors() {
        Optional<List<Actor>> optionalActor = Optional.ofNullable(actorRepository.findAll());
        if(!optionalActor.isEmpty()){
            List<Actor>  actorListFromDB =  optionalActor.get();
            List<ActorResponse> actorResponseList = new ArrayList<>();
            for(Actor actor : actorListFromDB){
                ActorResponse actorResponse = ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actor);
                List<ActorMovieJoinResponse> actorMovieJoinResponseList = new ArrayList<>();
                for(ActorMovieJoin actorMovieJoin : actor.getActorMovieJoinList()){
                    ActorMovieJoinResponse actorMovieJoinResponse = new ActorMovieJoinResponse();
                    actorMovieJoinResponse.setMovieResponse(MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(actorMovieJoin.getMovie()));
                    actorMovieJoinResponseList.add(actorMovieJoinResponse);
                }
                actorResponse.setActorMovieJoinResponseList(actorMovieJoinResponseList);
                actorResponseList.add(actorResponse);
            }
            return actorResponseList;
        }
        return "No Record Foud in the DB..";
    }
    public Object deleteActorById(Integer actorId){
        Optional<Actor> optionalActor = actorRepository.findById(actorId);
        if(optionalActor.isPresent()){
            actorRepository.deleteById(actorId);
            return  "Actor Record Deleted Succesfully...";
        }
        else
            return  "No Actor Found For Given Id..";
    }

public Object updateActor(ActorRequest actorRequest, int actorId) {
    Optional<Actor> optionalActor = actorRepository.findById(actorId);
    if (optionalActor.isPresent()) {
        Actor actor = optionalActor.get();

        if (actorRequest.getActorName() != null && !actorRequest.getActorName().isEmpty())
            actor.setActorName(actorRequest.getActorName());
        if (actorRequest.getActorMailId() != null && !actorRequest.getActorMailId().isEmpty())
            actor.setActorMailId(actorRequest.getActorMailId());
        if (actorRequest.getActorAddress() != null && !actorRequest.getActorAddress().isEmpty())
            actor.setActorAddress(actorRequest.getActorAddress());
        if (actorRequest.getActorGender() != null && !actorRequest.getActorGender().isEmpty())
            actor.setActorGender(ActorGender.valueOf(actorRequest.getActorGender()));
        if (actorRequest.getActorMobileNo() != null && !actorRequest.getActorMobileNo().isEmpty())
            actor.setActorMobileNo(actorRequest.getActorMobileNo());

        List<Movie> movieFromDBList = actor.getActorMovieJoinList().stream()
                .map(ActorMovieJoin::getMovie)
                .collect(Collectors.toList());

        List<Movie> movieListRequest = actorRequest.getActorMovieJoinRequestList().stream()
                .map(st -> MovieMapper.INSTANCE.movieRequestToMovieEntityMapping(st.getMovieRequest()))
                .collect(Collectors.toList());

        int minSize = Math.min(movieFromDBList.size(), movieListRequest.size());

        for (int i = 0; i < minSize; i++) {
            Movie existingMovie = movieFromDBList.get(i);
            Movie requestMovie = movieListRequest.get(i);

            if (requestMovie.getMovieTitle() != null && !requestMovie.getMovieTitle().isEmpty())
                existingMovie.setMovieTitle(requestMovie.getMovieTitle());
            if (requestMovie.getMovieDescription() != null && !requestMovie.getMovieDescription().isEmpty())
                existingMovie.setMovieDescription(requestMovie.getMovieDescription());
            if (requestMovie.getMovieDirector() != null && !requestMovie.getMovieDirector().isEmpty())
                existingMovie.setMovieDirector(requestMovie.getMovieDirector());
            if (requestMovie.getMovieProducer() != null && !requestMovie.getMovieProducer().isEmpty())
                existingMovie.setMovieProducer(requestMovie.getMovieProducer());
            existingMovie.setMovieBudget(requestMovie.getMovieBudget());
        }
        for (int i = minSize; i < movieListRequest.size(); i++) {
            Movie newMovie = movieListRequest.get(i);
            ActorMovieJoin newActorMovieJoin = new ActorMovieJoin();
            newActorMovieJoin.setMovie(newMovie);
            newActorMovieJoin.setActor(actor);
            actor.getActorMovieJoinList().add(newActorMovieJoin);
        }
        actorRepository.save(actor);
        return "Actor record Updated successfully...";
     } else
       return "Actor not found with ID: " + actorId;
  }

  public List<ActorResponse> getActorDataBasedOnSearch(ActorSearch actorSearch){
      List<Actor> actorList = actorRepository.findAll(actorRepository.findByCriteria(actorSearch)).stream().collect(Collectors.toList());
      if(actorList.isEmpty()){
          throw new CustomException("No records found for the given search criteria");
      }
      List<ActorResponse> actorResponseList = new ArrayList<>();
      for(Actor actor : actorList){
          ActorResponse actorResponse = ActorMapper.INSTANCE.actorEntityToActorResponseMapping(actor);
          List<ActorMovieJoinResponse>  actorMovieJoinResponseList = new ArrayList<>();
          for(ActorMovieJoin  actorMovieJoin: actor.getActorMovieJoinList()){
             ActorMovieJoinResponse  actorMovieJoinResponse = new ActorMovieJoinResponse();
             if(actorSearch.getActorName() != null && !actorSearch.getActorName().equals("")){
                 if(actorSearch.getActorName().equals(actorMovieJoin.getActor().getActorName())){
                     actorMovieJoinResponse.setMovieResponse(MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(actorMovieJoin.getMovie()));
                 }
             }
              if(actorSearch.getActorGender() != null && !actorSearch.getActorGender().equals("")){
                  if(actorSearch.getActorGender().equals(actorMovieJoin.getActor().getActorGender())){
                      actorMovieJoinResponse.setMovieResponse(MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(actorMovieJoin.getMovie()));
                  }
              }
             if(actorSearch.getMovieTitle() != null && !actorSearch.getMovieTitle().equals("")){
                 if(actorSearch.getMovieTitle().equals(actorMovieJoin.getMovie().getMovieTitle())){
                     actorMovieJoinResponse.setMovieResponse(MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(actorMovieJoin.getMovie()));
                 }
             }
              if(actorSearch.getMovieProducer() != null && !actorSearch.getMovieProducer().equals("")){
                  if(actorSearch.getMovieProducer().equals(actorMovieJoin.getMovie().getMovieProducer())){
                      actorMovieJoinResponse.setMovieResponse(MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(actorMovieJoin.getMovie()));
                  }
              }
              if(actorSearch.getMovieDirector() != null && !actorSearch.getMovieDirector().equals("")){
                  if(actorSearch.getMovieDirector().equals(actorMovieJoin.getMovie().getMovieDirector())){
                      actorMovieJoinResponse.setMovieResponse(MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(actorMovieJoin.getMovie()));
                  }
              }
              //actorMovieJoinResponse.setMovieResponse(MovieMapper.INSTANCE.movieEntityTOMovieResponseMapping(actorMovieJoin.getMovie()));
              actorMovieJoinResponseList.add(actorMovieJoinResponse);
          }
          actorResponse.setActorMovieJoinResponseList(actorMovieJoinResponseList);
          actorResponseList.add(actorResponse);
      }
      return actorResponseList;
  }


}
