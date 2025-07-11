package com.example.AccioNews.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveNewsRequest {
    private String author;
    private String title;
    private String url;
    private String urlToImage;
}
