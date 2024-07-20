package com.demo.M2M_Actor_Movie.mapping;

import com.demo.M2M_Actor_Movie.dto.ActorRequest;
import com.demo.M2M_Actor_Movie.dto.ActorResponse;
import com.demo.M2M_Actor_Movie.entity.Actor;
import com.demo.M2M_Actor_Movie.enums.ActorGender;
import com.demo.M2M_Actor_Movie.enums.ActorRole;
import com.demo.M2M_Actor_Movie.enums.MovieGenre;
import com.demo.M2M_Actor_Movie.enums.MovieStatus;
import com.demo.M2M_Actor_Movie.exception.CustomException;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActorMapper {
      ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

      @Mapping(source = "actorGender", target = "actorGender", qualifiedByName = "mapActorGender")
      @Mapping(source = "actorRole", target = "actorRole", qualifiedByName = "mapActorRole")
      Actor actorRequestToActorEntityMapping(ActorRequest actorRequest);
      ActorResponse actorEntityToActorResponseMapping(Actor actor);

      @Named("mapActorGender")
      default ActorGender mapActorGender(String actorGender) {

            try {
                  return ActorGender.valueOf(actorGender.toUpperCase());
            } catch (IllegalArgumentException e) {
                  throw new CustomException("Invalid actor gender: " + actorGender);
            }
      }
      @Named("mapActorRole")
      default ActorRole mapActorRole(String actorRole) {
            try {
                  return ActorRole.valueOf(actorRole.toUpperCase());
            } catch (IllegalArgumentException e) {
                  throw new CustomException("Invalid movie status: " + actorRole);
            }
      }
}



