package com.demo.M2M_Actor_Movie.entity;

import com.demo.M2M_Actor_Movie.enums.ActorGender;
import com.demo.M2M_Actor_Movie.enums.ActorRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
@Table(name="actor")
@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "demo")
    @Column(name = "id")
    @SequenceGenerator(name="demo", sequenceName = "my_sequense", allocationSize=1, initialValue = 1)
    private  Integer actorId;
    @Column(name="name")
    private String actorName;
    @Column(name="age")
    private Double actorAge;
    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private ActorGender actorGender;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private ActorRole actorRole;

    @Column(name="address")
    private String actorAddress;
    @Column(name="mobile")
    private String actorMobileNo;
    @Column(name="mailid")
    private String actorMailId;
    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActorMovieJoin> actorMovieJoinList;
    @CreationTimestamp
    @Column(name="created_date",updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name="updatedat_date")
    private LocalDateTime updatedDate;
}
