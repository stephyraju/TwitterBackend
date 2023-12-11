package com.springboot.twitterbackend.controller;

import com.springboot.twitterbackend.payload.ProfileDto;
import com.springboot.twitterbackend.payload.TweetsDto;
import com.springboot.twitterbackend.service.ProfileService;
import com.springboot.twitterbackend.service.TweetsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth/profile")
@Tag(
        name = "CRUD REST APIs for Profile"
)
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(TweetsController.class);
    private ProfileService profileService;
    private TweetsService tweetsService;

    public ProfileController(ProfileService profileService, TweetsService tweetsService) {
        this.profileService = profileService;
        this.tweetsService = tweetsService;
    }

    @GetMapping("/{userId}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable(name = "userId") long id) {
        logger.info("Request received at getProfileById Endpoint");
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Long> getUserFollowersById(@PathVariable(name = "userId") long id) {

        return ResponseEntity.ok(profileService.getUserFollowersById(id));
    }

    @GetMapping("/{userId}/ListOfOwnTweets")
    public List<TweetsDto> getTweetsByUserId(@PathVariable(value = "userId") Long user_id) {

        return tweetsService.getTweetsByUserId(user_id);
    }

    @GetMapping("/{userId}/OwnLikedRetweetedTweets")
    public List<TweetsDto> getAllOwnLikedAndRetweetedList(@PathVariable(value = "userId") Long user_id) {

        return tweetsService.getAllOwnLikedAndRetweetedList(user_id);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<ProfileDto> updateProfile(@Valid @RequestBody ProfileDto profileDto, @PathVariable(name = "userId") long id) {

        ProfileDto profileResponse = profileService.updateProfile(profileDto, id);

        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }
}