package com.springboot.twitterbackend.service;


import com.springboot.twitterbackend.payload.RetweetsDto;

public interface RetweetsService {
	
	 RetweetsDto createRetweet(long user_id, long tweet_id,RetweetsDto retweetsDto);
	 
	 Long getRetweetsCount(long tweet_id, RetweetsDto retweetsDto);
}
