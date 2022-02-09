package com.bank.app.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.app.model.User;
import com.bank.app.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserControlller {

    @Autowired
    private IUserService userService;

    @PostMapping("/saveuser")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return new ResponseEntity<User>(userService.createUser(user),HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{username}")
    public ResponseEntity<User> updateUser(@PathVariable("username") String userName,@RequestBody User user){
        return new ResponseEntity<User>(userService.updateUser(userName, user),HttpStatus.OK);
    }

    @PutMapping("/resendOtp/{username}")
    public ResponseEntity<User> resendOtp(@PathVariable("username") String userName){
        return new ResponseEntity<User>(userService.resendOtp(userName),HttpStatus.OK);
    }

    @PutMapping("/otp/{otp}/{username}")
    public ResponseEntity<User> validateUser(@PathVariable("otp") String otp,@PathVariable("username") String userName){
        return new ResponseEntity<User>(userService.validateUserByEmail(otp, userName),HttpStatus.OK);
    }

    @GetMapping("/getallUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<List<User>>(userService.getAllUser(),HttpStatus.OK);
    }
    @GetMapping("/getuserbyusername/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String userName){
        return  new ResponseEntity<User>(userService.getUserByUsername(userName),HttpStatus.OK);
    }
    
}