package com.springboot.twitterbackend.repository;

import com.springboot.twitterbackend.entity.FollowUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface FollowRepository extends JpaRepository<FollowUser, Long> {

	@Query(value = "SELECT count(id) FROM follow_user where followed_by = ?1 AND followed_to = ?2", nativeQuery = true)
     Long CheckIfAlreadyFollowed(long fromUserId,long toUserId);

	@Query("select Count(F.id) from FollowUser F JOIN User U on F.followedBy = ?1")
	Long findAllByFollowedTo(long id);

	@Query(value = "SELECT f.followedTo.id FROM FollowUser f WHERE f.followedBy.id = ?1")
	List<Long>findFollowedById(long user_id);

	@Query("select F.id from FollowUser F JOIN User U on F.followedBy = :fromUserId JOIN User US on F.followedTo = :toUserId WHERE U.id = :fromUserId AND US.id = :toUserId")
	Long CheckIfAlreadyFollowedId(long fromUserId, long toUserId);
}

