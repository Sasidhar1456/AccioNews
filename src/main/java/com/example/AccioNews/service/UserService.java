package com.example.AccioNews.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.AccioNews.config.JwtUtil;
import com.example.AccioNews.converter.UserConverter;
import com.example.AccioNews.dto.LogInRequestDto;
import com.example.AccioNews.dto.UserAuthResponseDto;
import com.example.AccioNews.dto.UserRequestDto;
import com.example.AccioNews.model.RefreshToken;
import com.example.AccioNews.model.User;
import com.example.AccioNews.repository.TokenRepository;
import com.example.AccioNews.repository.UserRepository;

import ch.qos.logback.core.subst.Token;


@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserConverter userConverter;





    public UserAuthResponseDto saveUser(UserRequestDto userRequestDto) {
        
        // 1. Convert DTO to entity
        User user = userConverter.convertUserRequestDtoToUser(userRequestDto);

        // 2. Save user to DB
        User savedUser = userRepository.save(user); // save() returns the saved user with ID

        // 3. Generate tokens
        String accessToken = jwtUtil.generateAccessToken(savedUser.getId(), savedUser.getUserName());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getId(), savedUser.getUserName());
        Instant expiry = jwtUtil.extractExpiration(refreshToken).toInstant();

        // 4. Save refresh token to TokenRepository
        tokenRepository.save(
            RefreshToken.builder()
                .user(savedUser)
                .token(refreshToken)
                .expiryData(expiry)
                .build()
        );

        // 5. Return response DTO
        return UserAuthResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .userName(savedUser.getUserName())
            .status("User registered successfully")
            .build();



    }



    public ResponseEntity<?> handleRefreshToken(String accessToken,String refreshToken) {

        
        if (accessToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

       
        

            //get refresh token from db
            RefreshToken tokenFromDb = tokenRepository.findByToken(refreshToken);

            //check it is null or not
            if (tokenFromDb == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not recognized");
            }


            //validate refresh token 
            if (tokenFromDb.getExpiryData().isBefore(java.time.Instant.now())) {
                tokenRepository.delete(tokenFromDb);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
            }

            // Extract user details
            Long userId = jwtUtil.extractUserId(refreshToken);
            String userName = jwtUtil.extractUserName(refreshToken);


            //generate  new accesstoken and return it
            String newAccessToken = jwtUtil.generateAccessToken(userId, userName);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));


            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
       
       
        
    }


    public void logout(String refreshToken) {
        RefreshToken token = tokenRepository.findByToken(refreshToken);
        if (token != null) {
            tokenRepository.delete(token);
        }
    }



    public UserAuthResponseDto logIn(LogInRequestDto logInRequestDto) {
        // 1. Find user by email
        User user = userRepository.findByEmail(logInRequestDto.getEmail());

        // 2. Check if user exists
        if (user == null) {
            throw new RuntimeException("User with this email does not exist");
        }

        // 3. Verify password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(logInRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 4. Check and Generate tokens

        RefreshToken refreshToken1 = tokenRepository.getByUserId(user.getId());

        if(refreshToken1 != null){
            tokenRepository.delete(refreshToken1);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUserName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUserName());
        Instant expiry = jwtUtil.extractExpiration(refreshToken).toInstant();

        // 5. Save refresh token
        tokenRepository.save(
            RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryData(expiry)
                .build()
        );

        // 6. Return response
        return UserAuthResponseDto.builder()
            .status("Login successful")
            .userName(user.getUserName())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }





}
