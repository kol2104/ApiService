package com.example.api_service.exception;

public class UserServiceUnavailable extends RuntimeException {
    public UserServiceUnavailable(){ super("User service Unavailable"); }
}
