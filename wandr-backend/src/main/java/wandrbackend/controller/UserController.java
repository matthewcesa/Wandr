package wandrbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wandrbackend.entity.User;
import wandrbackend.services.implementation.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl){
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUser(){
        return this.userServiceImpl.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("userId") Long userId){
        return this.userServiceImpl.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User createUser(@RequestBody User user){
        return this.userServiceImpl.createUser(user);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser (@PathVariable("userId") Long userId, @RequestBody User user){
        return this.userServiceImpl.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("userId") Long userId){
        this.userServiceImpl.deleteUser(userId);
    }
}

