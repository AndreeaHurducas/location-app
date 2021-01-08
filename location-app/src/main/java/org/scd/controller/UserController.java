package org.scd.controller;

import org.scd.config.exception.BusinessException;
import org.scd.model.User;
import org.scd.model.dto.UserLoginDTO;
import org.scd.model.dto.UserRegistrationDTO;
import org.scd.model.security.CustomUserDetails;
import org.scd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping(path = "/me")
    public ResponseEntity<User> getCurrentUser() {
        final User currentUser = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<User> loginUser(@RequestBody final UserLoginDTO userLoginDTO) throws BusinessException {
        return ResponseEntity.ok(userService.login(userLoginDTO));
    }

    @PostMapping(path = "/register")
    public ResponseEntity registerUser(@RequestBody final UserRegistrationDTO userRegistrationDTO) throws BusinessException{

        final URI location = ServletUriComponentsBuilder // http://localhost:8080
                .fromCurrentRequest()// http://localhost:8080/register
                .path("/{id}")// http://localhost:8080/register/{id}
                .buildAndExpand(userService.register(userRegistrationDTO).getId())// http://localhost:8080/register/user.getId()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping(path = "/login/admin")
    public ResponseEntity<User> loginAdmin(@RequestBody final UserLoginDTO userLoginDTO) throws BusinessException {
        return ResponseEntity.ok(userService.loginAdmin(userLoginDTO));
    }

}
