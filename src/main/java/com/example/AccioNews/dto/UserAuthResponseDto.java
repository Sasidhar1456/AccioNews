package com.example.AccioNews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthResponseDto {

    private String status;
    private String userName;
    private String accessToken;
    private String refreshToken;
}
