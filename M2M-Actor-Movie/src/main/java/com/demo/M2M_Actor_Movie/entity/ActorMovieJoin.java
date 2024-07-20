package com.demo.M2M_Actor_Movie.entity;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name="am_actormoviejoin")
public class ActorMovieJoin {
    @Id
    @Column(name="a_m_join")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer actorMovieJoinId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actor_id")
    private Actor actor;
}
