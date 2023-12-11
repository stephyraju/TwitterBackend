package com.springboot.twitterbackend.service.impl;


import com.springboot.twitterbackend.entity.FollowUser;
import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.exception.ResourceNotFoundException;
import com.springboot.twitterbackend.payload.FollowUserDto;
import com.springboot.twitterbackend.repository.FollowRepository;
import com.springboot.twitterbackend.repository.UserRepository;
import com.springboot.twitterbackend.service.FollowService;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


@Service
public  class FollowServiceImpl implements FollowService{


	private UserRepository userRepository;
	private FollowRepository followRepository;
	private ModelMapper modelMapper;



	public FollowServiceImpl( UserRepository userRepository,FollowRepository followRepository,
							  ModelMapper modelMapper) {
		super();

		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.followRepository=followRepository;
	}

@Override
	public FollowUserDto followUser(long fromUserId, long toUserId,FollowUserDto followUserDto) {

		FollowUser followUser = mapToEntity(followUserDto);

		User fromUserId1 = userRepository.findById(fromUserId).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", fromUserId));
		User toUserId1 = userRepository.findById(toUserId).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", toUserId));


		Long checkAlreadyExist = followRepository.CheckIfAlreadyFollowed(fromUserId,toUserId);

		if(checkAlreadyExist < 1) {

			followUser.setFollowedBy(fromUserId1);
			followUser.setFollowedTo(toUserId1);
			FollowUser newFollowUser = followRepository.save(followUser);

			long following_count = fromUserId1.getFollowing();
			following_count+=1;
			fromUserId1.setFollowing(following_count);
			userRepository.save(fromUserId1);

			long followers_count = toUserId1.getFollowersCount();
			followers_count+=1;
			toUserId1.setFollowersCount(followers_count);
			userRepository.save(fromUserId1);

			FollowUserDto followUserDto1 = mapToDTO(newFollowUser);
			System.out.println(followUserDto1);
			followUserDto1.setMsg_followed(true);
			return followUserDto1;

		}else {

			Long CheckIfAlreadyFollowedId = followRepository.CheckIfAlreadyFollowedId(fromUserId, toUserId);
			followRepository.deleteById(CheckIfAlreadyFollowedId);


			long following_count = fromUserId1.getFollowing();
			following_count=following_count-1;
			fromUserId1.setFollowing(following_count);
			userRepository.save(fromUserId1);

			long followers_count = toUserId1.getFollowersCount();
			followers_count=followers_count-1;
			toUserId1.setFollowersCount(followers_count);
			userRepository.save(fromUserId1);

			followUserDto.setMsg_followed(false);
			return followUserDto;
		}
	}

	private FollowUserDto mapToDTO(FollowUser followUser) {
		FollowUserDto followUserDto = modelMapper.map(followUser, FollowUserDto.class);
		return followUserDto;
	}

	private FollowUser mapToEntity(FollowUserDto followUserDto){
		FollowUser followUser = modelMapper.map(followUserDto, FollowUser.class);
		return  followUser;
	}

}