package com.springboot.twitterbackend.repository;

import com.springboot.twitterbackend.entity.Retweets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetweetsRepository extends JpaRepository<Retweets, Long> {

	List<Retweets> findByUserId(long userId);

//	@Query(value = "select T.id from Retweets R JOIN User U on R.user=U.id JOIN Tweets T on R.tweets=T.id WHERE U.id like ?1")
	List<Long> findRetweetsByUserId(long userId);

//	@Query(value = "select T.id from Retweets R JOIN User U on R.user=U.id JOIN Tweets T on R.tweets=T.id WHERE U.id = :userId")
//	List<Long> findRetweetsByUserId(long userId);


}
