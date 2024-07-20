package com.demo.M2M_Actor_Movie.repository;
import com.demo.M2M_Actor_Movie.dto.ActorSearch;
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
public interface ActorRepository  extends JpaRepository<Actor, Serializable>, JpaSpecificationExecutor<Actor> {
    default Specification<Actor> findByCriteria(final ActorSearch actorSearchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if(StringUtils.isNotEmpty(actorSearchRequest.getActorName())) {
                Expression<String> actorNameInTable = root.get("actorName");
                Expression<String> caseInsensitiveForName = criteriaBuilder.lower(actorNameInTable);
                predicateList.add(criteriaBuilder.like(caseInsensitiveForName,  actorSearchRequest.getActorName().toLowerCase() ));
            }
            if(StringUtils.isNotEmpty(actorSearchRequest.getActorGender())) {
                Expression<String> actorGenderInTable = root.get("actorGender");
                Expression<String> caseInsensitiveForGender = criteriaBuilder.lower(actorGenderInTable);
                predicateList.add(criteriaBuilder.like(caseInsensitiveForGender,  actorSearchRequest.getActorGender().toLowerCase() ));
            }
            Join<Actor, ActorMovieJoin> attMapperJoin = root.join("actorMovieJoinList", JoinType.INNER);
            Join<ActorMovieJoin, Movie> actorMovieJoinMovieJoin = attMapperJoin.join("movie");
            if(StringUtils.isNotEmpty(actorSearchRequest.getMovieDirector())) {
                Expression<String> directorNameInTable = actorMovieJoinMovieJoin.get("directorName");
                Expression<String> caseInSensitiveForDirectorName =criteriaBuilder.lower(directorNameInTable);
                predicateList.add(criteriaBuilder.like(caseInSensitiveForDirectorName,actorSearchRequest.getMovieDirector().toLowerCase()));
            }
            if(StringUtils.isNotEmpty(actorSearchRequest.getMovieTitle())) {
                Expression<String> movieTitleInTable = actorMovieJoinMovieJoin.get("movieTitle");
                Expression<String> caseInSensitiveForMovieTitle =criteriaBuilder.lower(movieTitleInTable);
                predicateList.add(criteriaBuilder.like(caseInSensitiveForMovieTitle,actorSearchRequest.getMovieTitle().toLowerCase()));
            }
            if(StringUtils.isNotEmpty(actorSearchRequest.getMovieProducer())) {
                Expression<String> movieProducerInTable = actorMovieJoinMovieJoin.get("movieProducer");
                Expression<String> caseInSensitiveForMovieProducer =criteriaBuilder.lower(movieProducerInTable);
                predicateList.add(criteriaBuilder.like(caseInSensitiveForMovieProducer,actorSearchRequest.getMovieProducer().toLowerCase()));
            }
            query.distinct(true);
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
    }
}
