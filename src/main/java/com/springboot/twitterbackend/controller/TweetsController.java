package com.springboot.twitterbackend.controller;

import com.springboot.twitterbackend.payload.TweetsDto;
import com.springboot.twitterbackend.service.TweetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class TweetsController {

    TweetsService tweetsService;
    private static final Logger logger = LoggerFactory.getLogger(TweetsController.class);

    @GetMapping("/welcome")
    public String welcome() {
        String text = "Welcome to Twitter Backend";
        return text;
    }

    public TweetsController(TweetsService tweetsService) {
        super();
        this.tweetsService = tweetsService;
    }

    @PostMapping("/users/{userId}/tweets/create")
    public ResponseEntity<TweetsDto> createTweet(@PathVariable(value = "userId") long user_id,
                                                 @Valid @RequestBody TweetsDto tweetsDto) {
        logger.info("Request received at create Endpoint");
        return new ResponseEntity<>(tweetsService.createTweet(user_id, tweetsDto), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/tweets")
    public List<TweetsDto> getTweetsByUserId(@PathVariable(value = "userId") Long user_id) {

        logger.info("Request received at yourEndpoint");
        return tweetsService.getTweetsByUserId(user_id);
    }

    @GetMapping("/users/{userId}/tweets/{tweetId}")
    public ResponseEntity<TweetsDto> getTweetById(@PathVariable(value = "userId") Long user_id,
                                                  @PathVariable(value = "tweetId") Long tweetId) {
        TweetsDto tweetsDto = tweetsService.getTweetById(user_id, tweetId);
        return new ResponseEntity<>(tweetsDto, HttpStatus.OK);
    }

}