package com.springboot.twitterbackend.service.impl;

import com.springboot.twitterbackend.controller.TweetsController;
import com.springboot.twitterbackend.entity.Likes;
import com.springboot.twitterbackend.entity.Tweets;
import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.exception.ResourceNotFoundException;
import com.springboot.twitterbackend.payload.LikesDto;
import com.springboot.twitterbackend.repository.LikesRepository;
import com.springboot.twitterbackend.repository.TweetsRepository;
import com.springboot.twitterbackend.repository.UserRepository;
import com.springboot.twitterbackend.service.LikesService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class LikesServiceImpl implements LikesService {
    private static final Logger logger = LoggerFactory.getLogger(TweetsController.class);
    private LikesRepository likesRepository;
    private UserRepository userRepository;
    private TweetsRepository tweetsRepository;
    private ModelMapper modelMapper;


    public LikesServiceImpl(LikesRepository likesRepository, UserRepository userRepository, TweetsRepository tweetsRepository,
                            ModelMapper modelMapper) {
        super();
        this.likesRepository = likesRepository;
        this.userRepository = userRepository;
        this.tweetsRepository = tweetsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public LikesDto likeTweet(long tweetId, long userId, LikesDto likesDto) {
        try {
            Likes likes = mapToEntity(likesDto);

            Tweets tweet = tweetsRepository.findById(tweetId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tweet", "id", tweetId));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

            List<Likes> existingLikes = likesRepository.findByUser_IdAndTweet_Id(userId, tweetId);

            if (existingLikes.isEmpty()) {
                likes.setLiked(true);
                likes.setUser(user);
                likes.setTweet(tweet);

                Likes newLike = likesRepository.save(likes);

                updateLikeCount(tweet, 1);

                return mapToDTO(newLike);
            } else {
                // User has already liked the tweet
                Likes existingLike = existingLikes.get(0);
                return mapToDTO(existingLike);
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            logger.error("Error liking tweet", e);
            throw new RuntimeException("Error liking tweet");
        }
    }


    private void updateLikeCount(Tweets tweet, int delta) {
        long likeCount = tweet.getLikes() + delta;
        tweet.setLikes(likeCount);
        tweetsRepository.save(tweet);
    }
    //END :: LIKE A TWEET REST API


    //START :: LIKE COUNT ON TWEET REST API
    @Override
    public Long getLikesCount(long tweetId) {
        // retrieve likes by tweetId
        List<Likes> likes = likesRepository.findByTweetId(tweetId);
        return likes.stream().count();
    }
    //END :: LIKE COUNT ON TWEET REST API


    private LikesDto mapToDTO(Likes likes) {
        logger.info("Likes entity before mapping: {}", likes);
        LikesDto likesDto = modelMapper.map(likes, LikesDto.class);
        logger.info("LikesDto after mapping: {}", likesDto);

        return likesDto;
    }

    private Likes mapToEntity(LikesDto likesDto) {
        logger.debug("Mapping LikesDto to Likes. LikesDto: {}", likesDto);
        Likes likes = modelMapper.map(likesDto, Likes.class);
        logger.debug("Mapped LikesDto to Likes. Likes: {}", likes);
        return likes;
    }

}