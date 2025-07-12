package com.example.AccioNews.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AccioNews.dto.LogInRequestDto;
import com.example.AccioNews.dto.UserAuthResponseDto;
import com.example.AccioNews.dto.UserRequestDto;
import com.example.AccioNews.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "https://short-news-rrqo.onrender.com")
@RestController
@RequestMapping("user/v1")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDto userRequestDto){

        try{

            UserAuthResponseDto userAuthResponseDto = userService.saveUser(userRequestDto);

            return ResponseEntity.ok(userAuthResponseDto);

        } catch(Exception e){
            return ResponseEntity.internalServerError().body("Error during signup: " + e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LogInRequestDto logInRequestDto){

        try{

            UserAuthResponseDto userAuthResponseDto = userService.logIn(logInRequestDto);

            return ResponseEntity.ok(userAuthResponseDto);

        } catch(Exception e){
            return ResponseEntity.internalServerError().body("Email or Password does not match");
        }

    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        String accessToken = body.get("accessToken");
        return userService.handleRefreshToken(accessToken, refreshToken);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        userService.logout(refreshToken);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

}
