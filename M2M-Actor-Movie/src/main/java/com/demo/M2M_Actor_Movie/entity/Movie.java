package com.demo.M2M_Actor_Movie.entity;

import com.demo.M2M_Actor_Movie.enums.MovieGenre;
import com.demo.M2M_Actor_Movie.enums.MovieStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Date;
import java.util.List;
@Data
@Entity
@Table(name="movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dem")
    @SequenceGenerator(name="dem", sequenceName = "my_seq", allocationSize=1, initialValue = 1)
    @Column(name = "id")
    private  Integer movieId;
    @Column(name="title")
    private String movieTitle;
    @Column(name="description")
    private String movieDescription;
    @Column(name="movieBudget")
    private int movieBudget;
    @Column(name="producer")
    private String movieProducer;
    @Column(name="director")
    private String movieDirector;
    @Enumerated(EnumType.STRING)
    private MovieGenre movieGenre;
    @Enumerated(EnumType.STRING)
    private MovieStatus movieStatus;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy")
    @Column(name="release_date")
    private Date movieReleaseDate;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActorMovieJoin> actorMovieJoinList;

    @CreationTimestamp
    @Column(name="created_date",updatable = false)
    private Date createdDate;
    @UpdateTimestamp
    @Column(name="updatedat_date")
    private Date updatedDate;
}
