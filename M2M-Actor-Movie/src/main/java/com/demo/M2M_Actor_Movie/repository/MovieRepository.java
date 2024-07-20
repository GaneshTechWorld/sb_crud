package com.demo.M2M_Actor_Movie.repository;

import com.demo.M2M_Actor_Movie.dto.ActorSearch;
import com.demo.M2M_Actor_Movie.dto.MovieSearch;
import com.demo.M2M_Actor_Movie.entity.Actor;
import com.demo.M2M_Actor_Movie.entity.ActorMovieJoin;
import com.demo.M2M_Actor_Movie.entity.Movie;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface MovieRepository  extends JpaRepository<Movie, Serializable>, JpaSpecificationExecutor<Movie> {
    default Specification<Movie> findByCriteria(final MovieSearch movieSearchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if(StringUtils.isNotEmpty(movieSearchRequest.getMovieDirector())) {
                Expression<String> movieDirectorNameInTable = root.get("movieDirector");
                Expression<String> caseInsensitiveForName = criteriaBuilder.lower(movieDirectorNameInTable);
                predicateList.add(criteriaBuilder.like(caseInsensitiveForName, movieSearchRequest.getMovieDirector().toLowerCase() ));
            }
            if(StringUtils.isNotEmpty(movieSearchRequest.getMovieProducer())) {
                Expression<String> movieProducerNameInTable = root.get("movieProducer");
                Expression<String> caseInsensitiveForProducer = criteriaBuilder.lower(movieProducerNameInTable);
                predicateList.add(criteriaBuilder.like(caseInsensitiveForProducer, movieSearchRequest.getMovieProducer().toLowerCase() ));
            }
            if(StringUtils.isNotEmpty(movieSearchRequest.getMovieTitle())) {
                Expression<String> movieTitleNameInTable = root.get("movieTitle");
                Expression<String> caseInsensitiveForMovieTitle = criteriaBuilder.lower(movieTitleNameInTable);
                predicateList.add(criteriaBuilder.like(caseInsensitiveForMovieTitle, movieSearchRequest.getMovieTitle().toLowerCase() ));
            }
            Join<Movie, ActorMovieJoin> attMapperJoin = root.join("actorMovieJoinList", JoinType.INNER);
            Join<ActorMovieJoin, Actor> actorMovieJoinMovieJoin = attMapperJoin.join("actor");
            if(StringUtils.isNotEmpty(movieSearchRequest.getActorGender())) {
                Expression<String>actorGenderInTable = actorMovieJoinMovieJoin.get("actorGender");
                Expression<String> caseInSensitiveForActorGender =criteriaBuilder.lower(actorGenderInTable);
                predicateList.add(criteriaBuilder.like(caseInSensitiveForActorGender,movieSearchRequest.getActorGender().toLowerCase()));
            }
            if(StringUtils.isNotEmpty(movieSearchRequest.getActorName())) {
                Expression<String> actorNameInTable = actorMovieJoinMovieJoin.get("actorName");
                Expression<String> caseInSensitiveForActorName =criteriaBuilder.lower(actorNameInTable);
                predicateList.add(criteriaBuilder.like(caseInSensitiveForActorName,movieSearchRequest.getActorName().toLowerCase()));
            }
            query.distinct(true);
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
    }
}
