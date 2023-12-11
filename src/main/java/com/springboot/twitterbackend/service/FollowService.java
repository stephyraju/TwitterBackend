package com.springboot.twitterbackend.service;


import com.springboot.twitterbackend.payload.FollowUserDto;

import javax.validation.Valid;

public interface FollowService {

	FollowUserDto followUser(long fromUserId, long toUserId, FollowUserDto followUserDto);


}
