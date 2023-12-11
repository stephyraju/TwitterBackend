package com.springboot.twitterbackend.repository;


import com.springboot.twitterbackend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTweetId(long tweetId);
}
