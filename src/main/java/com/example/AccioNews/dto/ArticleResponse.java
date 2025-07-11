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
public class ArticleResponse {

    String author;
    String title;
    String url;
    String urlToImage;
    String content;
    Boolean isSaved;

}
