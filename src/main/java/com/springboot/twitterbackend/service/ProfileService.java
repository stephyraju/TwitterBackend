package com.springboot.twitterbackend.service;


import com.springboot.twitterbackend.payload.ProfileDto;

public interface ProfileService {

	ProfileDto getProfileById(long id);

	ProfileDto updateProfile(ProfileDto profileDto, long id);
	
	 Long getUserFollowersById(long id);

    
}
