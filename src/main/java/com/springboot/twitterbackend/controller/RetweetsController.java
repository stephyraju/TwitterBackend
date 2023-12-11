package com.springboot.twitterbackend.controller;

import com.springboot.twitterbackend.payload.RetweetsDto;
import com.springboot.twitterbackend.repository.RetweetsRepository;
import com.springboot.twitterbackend.service.RetweetsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class RetweetsController {
	RetweetsRepository retweetsRepository;
	RetweetsService  retweetsService;

	public RetweetsController(RetweetsRepository retweetsRepository, RetweetsService retweetsService) {
		super();
		this.retweetsRepository = retweetsRepository;
		this.retweetsService = retweetsService;
	}

	@PostMapping("retweet/{tweetId}/{userId}")
	public  ResponseEntity<RetweetsDto> createRetweet(@Valid @PathVariable(value="userId") long user_id,
													  @PathVariable(value="tweetId") long tweet_id,
													  @RequestBody RetweetsDto retweetsDto){

		return new ResponseEntity<>(retweetsService.createRetweet(user_id, tweet_id,retweetsDto), HttpStatus.CREATED);
	}
	@GetMapping("retweetCount/{tweetId}")
	public ResponseEntity<Long> getRetweetsCount(@Valid @PathVariable(value="tweetId") long tweet_id,
												 @RequestBody RetweetsDto retweetsDto
	){

		Long noOfRetweets = retweetsService.getRetweetsCount(tweet_id,retweetsDto);
		return new ResponseEntity<Long>(noOfRetweets, HttpStatus.OK);

	}
}