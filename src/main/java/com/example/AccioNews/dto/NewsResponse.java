package com.example.AccioNews.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {

    int totalResults;
    ArrayList<ArticleResponse>  articles = new ArrayList<>();
    
}
