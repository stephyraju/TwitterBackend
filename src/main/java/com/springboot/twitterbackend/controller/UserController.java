package com.springboot.twitterbackend.controller;

import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("users")
    public List<User> getUsers(){
        return this.userRepository.findAll();
    }
//@GetMapping("users")
//public List<User> getUsers() {
//    List<User> userList = new ArrayList<>();
//    userList.add(new User(1L, "John", "john@gmail.com"));
//    userList.add(new User(2L, "Julie", "julie@gmail.com"));
//    return userList;
//}

}
