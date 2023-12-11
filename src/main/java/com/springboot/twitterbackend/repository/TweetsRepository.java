package com.springboot.twitterbackend.repository;

import com.springboot.twitterbackend.entity.Tweets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetsRepository extends JpaRepository<Tweets, Long> {
	
	List<Tweets> findByUserId(long user_id);
	Long findRetweetsById(long tweet_id);
}
