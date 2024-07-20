package com.demo.M2M_Actor_Movie.controller;

import com.demo.M2M_Actor_Movie.dto.ActorRequest;
import com.demo.M2M_Actor_Movie.dto.ActorSearch;
import com.demo.M2M_Actor_Movie.entity.Actor;
import com.demo.M2M_Actor_Movie.exception.CustomException;
import com.demo.M2M_Actor_Movie.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
public class ActorController {
    @Autowired
    ActorService actorService;
    @PostMapping(value = "saveActorInfo", consumes = "application/json")
    public ResponseEntity<Object> saveActor(@Valid @RequestBody ActorRequest actor, BindingResult bindingResult) {
        System.out.println("Received Actor data: " + actor);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessageBuilder = new StringBuilder("Validation failed:\n");
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errorMessageBuilder.append(fieldName).append(": ").append(errorMessage).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessageBuilder.toString());
        }
        actorService.saveActorInfo(actor);
        return ResponseEntity.ok("Actor saved successfully");
    }
    @GetMapping("getAllActors")
    public ResponseEntity<Object> getAllActors(){
        Object obj = actorService.getAllActors();
        if(obj instanceof Actor){
            return new ResponseEntity<>(actorService.getAllActors(), HttpStatusCode.valueOf(200));
        }else
            throw new CustomException("Internal Server Problem");
    }
    @DeleteMapping("deleteActor")
    public ResponseEntity<Object> deleteActorById(@RequestParam Integer actorId){
        String status;
        if(!actorId.equals("")  && actorId != null){
            return new ResponseEntity<>(actorService.deleteActorById(actorId), HttpStatus.OK);
        }
        return new ResponseEntity<>("Please Enter Proper Id..", HttpStatus.BAD_REQUEST);
    }
    @PutMapping("updateActor")
    public ResponseEntity<Object> updateActor(@RequestBody ActorRequest actorRequest, @RequestParam final int actorId){
        if(actorRequest != null &&  actorId >0){
            return new ResponseEntity<>( actorService.updateActor(actorRequest,actorId),HttpStatus.OK);
        }
        return new ResponseEntity<>("Please Enter Proper Id ",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("actorSearch")
    public ResponseEntity<Object> getActorBasedOnSearch(@RequestBody ActorSearch actorSearch){
        if(actorSearch !=null){
            return new ResponseEntity<>( actorService.getActorDataBasedOnSearch(actorSearch),HttpStatus.OK);
        }
        return new ResponseEntity<>("Record Not Updated..",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
