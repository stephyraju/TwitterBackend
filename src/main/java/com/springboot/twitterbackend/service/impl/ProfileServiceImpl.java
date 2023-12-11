package com.springboot.twitterbackend.service.impl;

import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.exception.ResourceNotFoundException;
import com.springboot.twitterbackend.payload.ProfileDto;
import com.springboot.twitterbackend.repository.FollowRepository;
import com.springboot.twitterbackend.repository.UserRepository;
import com.springboot.twitterbackend.service.ProfileService;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


@Service
public class ProfileServiceImpl implements ProfileService{

	private UserRepository userRepository;
	private ModelMapper mapper;


	private FollowRepository followRepository;
	public ProfileServiceImpl(UserRepository userRepository, ModelMapper mapper,FollowRepository followRepository) {
		super();
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.followRepository =followRepository;
	}

	//START :: GET PROFILE BY ID
	@Override
	public ProfileDto getProfileById(long id) {

		User profile = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile", "id", id));
		return mapToDTO(profile);
	}
	//END :: GET PROFILE BY ID



	//START :: GET USER FOLLOWERS BY USER ID
	@Override
	public Long getUserFollowersById(long id) {

		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile", "id", id));
		long noOfFollowers = 0;
		noOfFollowers = followRepository.findAllByFollowedTo(id);
		return noOfFollowers;

	}
	//END :: GET USER FOLLOWERS BY USER ID



	//START :: UPDATE PROFILE OF THE USER
	@Override
	public ProfileDto updateProfile(ProfileDto profileDto, long id) {
		// get USER by id from the database
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profile", "id", id));

		user.setContact(profileDto.getContact());
		user.setName(profileDto.getName());
		user.setBio(profileDto.getBio());

		User updatedProfile = userRepository.save(user);
		return mapToDTO(updatedProfile);

	}
	//END :: UPDATE PROFILE OF THE USER


	// convert Entity into DTO
	private ProfileDto mapToDTO(User profile){

		ProfileDto profileDto = mapper.map(profile, ProfileDto.class);
		return profileDto;
	}

	// convert DTO to entity
	//    private User mapToEntity(ProfileDto profileDto){
	//    	User profile = mapper.map(profileDto, User.class);
	//        return profile;
	//  }

}