package com.misha.cookbook.controller;

import com.misha.cookbook.model.User;
import com.misha.cookbook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class CookbookController {

    private final Logger log = LoggerFactory.getLogger(CookbookController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        log.info("REST API getUser() function called");

        User user = userService.findById(id);
        if (user == null) {
            log.error("User not found", "Id", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/user/")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("REST API listAllUsers() function called");

        List<User> allUsers = userService.findAll();
        if (CollectionUtils.isEmpty(allUsers)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PostMapping(value = "/user/")
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder uriBuilder) {

        // TODO put API logging to some interceptor
        log.info("REST API createNewUser() function called");

        if (userService.alreadyExists(user)) {
            log.error("User already exists", "UserEmail", user.getEmail());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        log.info("REST API deleteUser() function called");

        if (userService.findById(id) == null) {
            log.error("User with given id does not exist", "Id", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
