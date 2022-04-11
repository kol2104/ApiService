package com.example.api_service.controller;

import com.example.api_service.exception.UserServiceUnavailable;
import com.example.api_service.model.User;
import com.example.api_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.ConnectException;
import java.util.List;

@RestController
@RequestMapping("/api/service")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable("id") String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsersByName(@RequestParam(name = "name") String name) {
        List<User> userList = userService.getUsersByName(name);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PatchMapping
    public ResponseEntity<User> updateUser(@RequestParam(name = "id") String id, @RequestBody User user) throws JsonProcessingException {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus>  deleteUserById (@PathVariable("id") String id) {
        userService.deleteUserById(id);
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity handleException(HttpClientErrorException exception) {
        if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConnectException.class})
    public ResponseEntity handleException(ConnectException exception) {
        return new ResponseEntity(new UserServiceUnavailable().getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
