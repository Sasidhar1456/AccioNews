package com.example.AccioNews.converter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.AccioNews.dto.UserRequestDto;
import com.example.AccioNews.model.User;


@Component
public class UserConverter {

    public User convertUserRequestDtoToUser(UserRequestDto userRequestDto){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        return  User.builder()
                .userName(userRequestDto.getUserName())
                .password(encodedPassword)
                .email(userRequestDto.getEmail())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .build();
    }
}
