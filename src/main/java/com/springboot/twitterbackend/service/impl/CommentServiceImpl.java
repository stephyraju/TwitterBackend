package com.springboot.twitterbackend.service.impl;


import com.springboot.twitterbackend.entity.Comment;
import com.springboot.twitterbackend.entity.Tweets;
import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.exception.ResourceNotFoundException;
import com.springboot.twitterbackend.payload.CommentDto;
import com.springboot.twitterbackend.repository.CommentRepository;
import com.springboot.twitterbackend.repository.TweetsRepository;
import com.springboot.twitterbackend.repository.UserRepository;
import com.springboot.twitterbackend.service.CommentService;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private TweetsRepository tweetsRepository;
    private ModelMapper mapper;
    private UserRepository userRepository;
    public CommentServiceImpl(CommentRepository commentRepository, TweetsRepository tweetsRepository, ModelMapper mapper,UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tweetsRepository = tweetsRepository;
        this.mapper = mapper;
        this.userRepository =userRepository;
    }

    @Override
    public CommentDto createComment(long tweetId, long userId,CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId));

        // retrieve post entity by id
        Tweets tweet = tweetsRepository.findById(tweetId).orElseThrow(
                () -> new ResourceNotFoundException("Tweet", "id", tweetId));

        // set post to comment entity
        comment.setTweet(tweet);
        comment.setUser(user);

        // comment entity to DB
        Comment newComment =  commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByTweetId(long tweetId) {
        // retrieve comments by tweetId
        List<Comment> comments = commentRepository.findByTweetId(tweetId);

        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public Long getCommentsByTweetIdCount(long tweetId) {
        // retrieve comments by tweetId
        List<Comment> comments = commentRepository.findByTweetId(tweetId);

        // convert list of comment entities to list of comment dto's
        return comments.stream().count();
    }
    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        return  commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto, Comment.class);
        return  comment;
    }
//}
//
//    private CommentDto mapToDTO(Comment comment){
//        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//        return  commentDto;
//    }
//
//    private Comment mapToEntity(CommentDto commentDto){
//        Comment comment = mapper.map(commentDto, Comment.class);
//        return  comment;
//    }
}
