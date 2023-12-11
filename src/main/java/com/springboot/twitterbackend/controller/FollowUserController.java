package com.springboot.twitterbackend.controller;

import com.springboot.twitterbackend.payload.FollowUserDto;
import com.springboot.twitterbackend.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class FollowUserController {
    private FollowService followService;


    public FollowUserController(FollowService followService) {
        super();
        this.followService = followService;
    }

    @PostMapping("/followUnfollow/{fromUserId}/{toUserId}")
    public ResponseEntity<FollowUserDto> followUser(@PathVariable(value = "fromUserId") long fromUserId,
                                                    @PathVariable(value = "toUserId") long toUserId,
                                                    @Valid @RequestBody FollowUserDto followUserDto) {

        return new ResponseEntity<>(followService.followUser(fromUserId, toUserId, followUserDto), HttpStatus.CREATED);
    }
}
