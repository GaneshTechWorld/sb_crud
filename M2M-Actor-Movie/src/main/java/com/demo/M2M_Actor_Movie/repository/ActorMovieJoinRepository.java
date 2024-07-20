package com.demo.M2M_Actor_Movie.repository;

import com.demo.M2M_Actor_Movie.entity.ActorMovieJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorMovieJoinRepository extends JpaRepository<ActorMovieJoin, Integer> {
}