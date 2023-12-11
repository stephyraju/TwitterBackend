package com.springboot.twitterbackend.repository;

import com.springboot.twitterbackend.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

	List<Likes> findByUserId(long userId);

	List<Likes> findByTweetId(long tweetId);

	List<Likes> findByUser_IdAndTweet_Id(Long userId, Long tweetId);

	@Query(value = "SELECT count(id) FROM likes where tweet_id = :tweetId AND user_id = :userId", nativeQuery = true)
	long countOfAlreadyLiked(@Param("tweetId") long tweetId, @Param("userId") long userId);

	@Query(value = "SELECT L.id from Likes L WHERE L.user.id = :userId AND L.tweet.id = :tweetId")
	Long checkIfAlreadyLikedId(@Param("tweetId") long tweetId, @Param("userId") long userId);

	@Query("SELECT l.tweet.id FROM Likes l WHERE l.user.id = :userId")
	List<Long> findLikesById(@Param("userId") Long userId);
}

