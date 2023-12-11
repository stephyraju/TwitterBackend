package com.springboot.twitterbackend.service;


import com.springboot.twitterbackend.payload.CommentDto;

import java.util.List;

public interface CommentService {
	
	
      CommentDto createComment(long tweetId, long userId, CommentDto commentDto);

    List<CommentDto> getCommentsByTweetId(long tweetId);
    
    Long getCommentsByTweetIdCount(long tweetId);

   
}
