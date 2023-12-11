package com.springboot.twitterbackend.repository;

import com.springboot.twitterbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("select u.id from User u where u.email= :email")
    Long findUseridByEmail(String email);
}
