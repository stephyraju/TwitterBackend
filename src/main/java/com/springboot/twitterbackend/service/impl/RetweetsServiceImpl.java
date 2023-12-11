package com.springboot.twitterbackend.service.impl;

import com.springboot.twitterbackend.entity.Retweets;
import com.springboot.twitterbackend.entity.Tweets;
import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.exception.ResourceNotFoundException;
import com.springboot.twitterbackend.payload.RetweetsDto;
import com.springboot.twitterbackend.repository.RetweetsRepository;
import com.springboot.twitterbackend.repository.TweetsRepository;
import com.springboot.twitterbackend.repository.UserRepository;
import com.springboot.twitterbackend.service.RetweetsService;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

@Service
public class RetweetsServiceImpl implements RetweetsService{

	private ModelMapper modelMapper;
	private UserRepository userRepository;
	private RetweetsRepository retweetsRepository;
	private TweetsRepository tweetsRepository;



	public RetweetsServiceImpl(ModelMapper modelMapper, UserRepository userRepository,
							   RetweetsRepository retweetsRepository, TweetsRepository tweetsRepository) {
		super();
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.retweetsRepository = retweetsRepository;
		this.tweetsRepository = tweetsRepository;
	}

	//START :: RETWEET AN OTHER USERS TWEET
	@Override
	public RetweetsDto createRetweet(long user_id, long tweet_id, RetweetsDto retweetsDto) {

		Retweets retweets = mapToEntity(retweetsDto);

		User userid = userRepository.findById(user_id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", user_id));

		Tweets tweetId = tweetsRepository.findById(tweet_id).orElseThrow(
				() -> new ResourceNotFoundException("Tweet", "id", tweet_id));

		retweets.setUser(userid);

		retweets.setTweets(tweetId);

		Retweets newRetweet =  retweetsRepository.save(retweets);

		long retweet_count = tweetId.getRetweets();
		retweet_count+=1;
		tweetId.setRetweets(retweet_count);
		tweetsRepository.save(tweetId);
		retweetsDto = mapToDTO(newRetweet);
		retweetsDto.setUser_id(user_id);
		retweetsDto.setTweet_id(tweet_id);
		return retweetsDto;

	}
	@Override
	public Long getRetweetsCount(long tweet_id, RetweetsDto retweetsDto) {
		// retrieve tweet entity by id
		Tweets tweetId = tweetsRepository.findById(tweet_id).orElseThrow(
				() -> new ResourceNotFoundException("Tweet", "id", tweet_id));

		long noOfRetweets = tweetId.getRetweets();

		return noOfRetweets;
	}
	//END :: GET RETWEET COUNT BY TWEET ID


	private RetweetsDto mapToDTO(Retweets newRetweet) {
		RetweetsDto retweetsDto = modelMapper.map(newRetweet,RetweetsDto.class);
		return retweetsDto;
	}

	private Retweets mapToEntity(RetweetsDto retweetsDto) {

		Retweets retweets = modelMapper.map(retweetsDto,Retweets.class);
		return retweets;
	}

}