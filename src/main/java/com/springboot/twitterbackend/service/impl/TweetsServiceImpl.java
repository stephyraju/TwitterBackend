package com.springboot.twitterbackend.service.impl;

import com.springboot.twitterbackend.controller.TweetsController;
import com.springboot.twitterbackend.entity.Comment;
import com.springboot.twitterbackend.entity.Tweets;
import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.exception.BlogAPIException;
import com.springboot.twitterbackend.exception.ResourceNotFoundException;
import com.springboot.twitterbackend.payload.TweetsDto;
import com.springboot.twitterbackend.repository.CommentRepository;
import com.springboot.twitterbackend.repository.FollowRepository;
import com.springboot.twitterbackend.repository.LikesRepository;
import com.springboot.twitterbackend.repository.RetweetsRepository;
import com.springboot.twitterbackend.repository.TweetsRepository;
import com.springboot.twitterbackend.repository.UserRepository;
import com.springboot.twitterbackend.service.TweetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TweetsServiceImpl implements TweetsService{
	private static final Logger logger = LoggerFactory.getLogger(TweetsController.class);
	private TweetsRepository tweetsRepository;
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private LikesRepository likesRepository;
	private FollowRepository followRepository;
	private RetweetsRepository retweetsRepository;
	private CommentRepository commentRepository;



	public TweetsServiceImpl(TweetsRepository tweetsRepository, UserRepository userRepository,
							 ModelMapper modelMapper ,
							 LikesRepository likesRepository,CommentRepository commentRepository,
							 FollowRepository followRepository,RetweetsRepository retweetsRepository) {
		super();
		this.tweetsRepository = tweetsRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.likesRepository=likesRepository;
		this.followRepository =followRepository;
		this.retweetsRepository=retweetsRepository;
		this.commentRepository =commentRepository;
	}

	@Override
	public TweetsDto createTweet(long user_id, TweetsDto tweetsDto) {

		Tweets tweet = mapToEntity(tweetsDto);

		User userid = userRepository.findById(user_id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", user_id));


		tweet.setUser(userid);
		if(tweet.getContent().length()>280) {

			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Length must be between 0 to 280 characters!.");
		}

		logger.info("User ID during tweet creation: " + user_id);
		Tweets newTweet =  tweetsRepository.save(tweet);
		TweetsDto newTweetDto = mapToDTO(newTweet);
		newTweetDto.setUsername(userid.getUsername());
		newTweetDto.setUser_id(user_id);
		return newTweetDto;
	}



	//START :: GET ALL TWEETS BY USER ID
	@Override
	public List<TweetsDto> getTweetsByUserId(long user_id) {
		List<Tweets> tweets = tweetsRepository.findByUserId(user_id);

		List<TweetsDto> tweetDtoList = tweets.stream()
				.sorted(Comparator.comparing(Tweets::getId).reversed())
				.map(tweet -> mapToDTO(tweet))
				.collect(Collectors.toList());
		for (TweetsDto userD : tweetDtoList) {
			Long tweetId = userD.getId();
			Tweets tweet = tweetsRepository.findById(tweetId).orElseThrow(() ->
					new ResourceNotFoundException("Tweet", "id", tweetId));

			User fTweeterUser = userRepository.findById(tweet.getUser().getId()).orElseThrow(
					() -> new ResourceNotFoundException("User", "id", tweet.getUser().getId()));
			//comments of each TWEET
			List<Comment> allTweetComments = commentRepository.findByTweetId(tweetId);
			if(allTweetComments!=null) {
				List<String> listOfComments = new LinkedList<String>();
				for (Comment commentId : allTweetComments) {
					String allTweetCommentMsg  = commentId.getCommentMsg();
					listOfComments.add(allTweetCommentMsg);
				}

				userD.setComments(listOfComments);
			}

			userD.setUsername(fTweeterUser.getName());
		}
		return tweetDtoList;
	}
	@Override
	public List<TweetsDto> getAllOwnLikedAndRetweetedList(long user_id) {


		List<Tweets> Usertweets = tweetsRepository.findByUserId(user_id);


		List<Long> UserLikedtweetsId = likesRepository.findLikesById(user_id);
		List<Tweets> likedTweets =tweetsRepository.findAllById(UserLikedtweetsId);
		Usertweets.addAll(likedTweets);

		List<Long> UserRetweetsId = retweetsRepository.findRetweetsByUserId(user_id);
		System.out.println(UserRetweetsId);
		List<Tweets> RetweetedTweets =tweetsRepository.findAllById(UserRetweetsId);
		Usertweets.addAll(RetweetedTweets);

		return Usertweets.stream().map(tweet -> mapToDTO(tweet)).collect(Collectors.toList());

	}
	@Override
	public List<TweetsDto> getAllUserAndFollowedTweets(long user_id) {

		User user = userRepository.findById(user_id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", user_id));

		List<Tweets> userTweets = tweetsRepository.findByUserId(user_id);


		List<Long> userFollowedIds = followRepository.findFollowedById(user_id);


		for (Long userId : userFollowedIds) {

				List<Tweets> followedUserTweets = tweetsRepository.findByUserId(userId);
				userTweets.addAll(followedUserTweets);
		}
		logger.info("User ID: {}", user_id);
		logger.info("Followed User IDs: {}", userFollowedIds);

		// Sorting and mapping to DTO
		List<TweetsDto> tweetDtoList = userTweets.stream()
				.sorted(Comparator.comparing(Tweets::getId).reversed())
				.map(this::mapToDTO)
				.collect(Collectors.toList());

		// Getting USERNAME for EACH TWEET
		for (TweetsDto tweetDto : tweetDtoList) {
			Long tweetId = tweetDto.getId();
			Tweets tweet = tweetsRepository.findById(tweetId).orElseThrow(() ->
					new ResourceNotFoundException("Tweet", "id", tweetId));

			User tweetUser = tweet.getUser();

			// Set the username directly from the tweet user
			tweetDto.setUsername(tweetUser.getName());
			tweetDto.setUser_id(tweetUser.getId());

			// Comments of each TWEET
			List<Comment> allTweetComments = commentRepository.findByTweetId(tweetId);
			if (allTweetComments != null) {
				List<String> listOfComments = allTweetComments.stream()
						.map(Comment::getCommentMsg)
						.collect(Collectors.toList());

				tweetDto.setComments(listOfComments);
			}
		}

		return tweetDtoList;
	}
	@Override
	public TweetsDto getTweetById(Long user_id, Long tweetId) {
		// retrieve user entity by id
		User user = userRepository.findById(user_id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", user_id));

		// retrieve TWEET by id
		Tweets tweet = tweetsRepository.findById(tweetId).orElseThrow(() ->
				new ResourceNotFoundException("Tweet", "id", tweetId));

		if(!tweet.getUser().getId().equals(user.getId())){
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Tweet does not belong to user");
		}

		return mapToDTO(tweet);
	}

	@Override
	public TweetsDto updateTweet(Long user_id, long tweetId, TweetsDto tweetRequest) {
		// retrieve user entity by id
		User user = userRepository.findById(user_id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", user_id));

		// retrieve TWEET by id
		Tweets tweet = tweetsRepository.findById(tweetId).orElseThrow(() ->
				new ResourceNotFoundException("Tweet", "id", tweetId));

		if(!tweet.getUser().getId().equals(user.getId())){
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Tweet does not belongs to user");
		}

		tweet.setContent(tweetRequest.getContent());
		tweet.setImage(tweetRequest.getImage());
		Tweets updatedTweet = tweetsRepository.save(tweet);
		return mapToDTO(updatedTweet);
	}
	@Override
	public void deleteTweet(Long user_id, Long tweetId) {
		// retrieve user entity by id
		User user = userRepository.findById(user_id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", user_id));

		// retrieve TWEET by id
		Tweets tweet = tweetsRepository.findById(tweetId).orElseThrow(() ->
				new ResourceNotFoundException("Tweet", "id", tweetId));

		if(!tweet.getUser().getId().equals(user.getId())){
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Tweet does not belongs to user");
		}

		tweetsRepository.delete(tweet);

	}

	private TweetsDto mapToDTO(Tweets tweets){
		TweetsDto tweetsDto = modelMapper.map(tweets, TweetsDto.class);

		return  tweetsDto;
	}

	private Tweets mapToEntity(TweetsDto tweetsDto){
		Tweets tweets = modelMapper.map(tweetsDto, Tweets.class);
		return  tweets;
	}

}
