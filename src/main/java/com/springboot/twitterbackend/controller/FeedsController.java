package com.springboot.twitterbackend.controller;


import com.springboot.twitterbackend.payload.TweetsDto;
import com.springboot.twitterbackend.service.TweetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class FeedsController {
    private static final Logger logger = LoggerFactory.getLogger(TweetsController.class);
    private TweetsService tweetsService;

    public FeedsController(TweetsService tweetsService) {

        this.tweetsService = tweetsService;
    }

    @GetMapping("/userAndFollowedTweets/{userId}")
    public ResponseEntity<List<TweetsDto>> userAndFollowedTweets(@PathVariable(value = "userId") long user_id) {
        List<TweetsDto> tweetsList = tweetsService.getAllUserAndFollowedTweets(user_id);
        return ResponseEntity.ok(tweetsList);
    }
}