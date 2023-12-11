package com.springboot.twitterbackend.controller;

import com.springboot.twitterbackend.payload.LikesDto;
import com.springboot.twitterbackend.service.LikesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class LikesController {
    private LikesService likesService;

    public LikesController(LikesService likesService) {
        super();
        this.likesService = likesService;
    }

    @PostMapping("/likes/{tweetId}/{userId}")
    public ResponseEntity<LikesDto> likeTweet(@PathVariable(value = "tweetId") long tweetId,
                                              @PathVariable(value = "userId") long userId,
                                              @Valid @RequestBody LikesDto likesDto) {
        System.out.println("Received Likes DTO: " + likesDto.toString());
        return new ResponseEntity<>(likesService.likeTweet(tweetId, userId, likesDto), HttpStatus.CREATED);
    }


    @GetMapping("/tweets/{tweetId}/likesCount")
    public Long getLikesCount(@PathVariable(value = "tweetId") Long tweetId) {

        return likesService.getLikesCount(tweetId);
    }


}