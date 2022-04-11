package com.example.api_service.service;

import com.example.api_service.model.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Value("#{'${user-service-url}' + '${user-service-path}'}")
    private String userUrl;

    @Autowired
    RestTemplate restTemplate;

    public User getUserById(String id) {
        String url = userUrl + "/" + id;
        return restTemplate.getForObject(url, User.class);
    }

    public List<User> getUsersByName(String name) {
        String url = userUrl + "/name?name=" + name;
        return Arrays.asList(restTemplate.getForObject(url, User[].class));
    }

    public User createUser(User user) {
        return restTemplate.postForObject(userUrl, user, User.class);
    }

    public ResponseEntity<User> updateUser(String id, User user) throws JsonProcessingException {
        String url = userUrl + "/" + id;
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/merge-patch+json");
        HttpEntity<User> request = new HttpEntity(user, headers);

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate.exchange(url, HttpMethod.PATCH, request, User.class, id);
    }

    public void deleteUserById(String id) {
        String url = userUrl + "/" + id;
        restTemplate.delete(url);
    }
}
