package com.springboot.twitterbackend.service;


import com.springboot.twitterbackend.payload.LikesDto;


import javax.validation.Valid;

public interface LikesService {

	LikesDto likeTweet(long tweetId, long userId, @Valid LikesDto likesDto);
	
	  Long getLikesCount(long tweetId);
}
