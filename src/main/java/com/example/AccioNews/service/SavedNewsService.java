package com.example.AccioNews.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AccioNews.config.JwtUtil;
import com.example.AccioNews.dto.ArticleResponse;
import com.example.AccioNews.dto.SaveNewsRequest;
import com.example.AccioNews.model.SavedNews;
import com.example.AccioNews.model.User;
import com.example.AccioNews.repository.SavedNewsRepository;
import com.example.AccioNews.repository.UserRepository;

@Service
public class SavedNewsService {

    @Autowired
    private SavedNewsRepository savedNewsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public List<ArticleResponse> getSavedNews(String accessToken) {
        List<SavedNews> savedNewsList = savedNewsRepository.findByUserId(jwtUtil.extractUserId(accessToken));

        return savedNewsList.stream().map(news -> ArticleResponse.builder()
                .author(news.getAuthor())
                .title(news.getTitle())
                .url(news.getUrl())
                .urlToImage(news.getUrlToImage())
                .content(news.getContent())
                .isSaved(true) 
                .build()
    ).collect(Collectors.toList());
    }


    public void saveNews(SaveNewsRequest saveNewsRequest,String accessToken) {
        User user = userRepository.findById(jwtUtil.extractUserId(accessToken))
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavedNews savedNews = new SavedNews();
        savedNews.setAuthor(saveNewsRequest.getAuthor());
        savedNews.setTitle(saveNewsRequest.getTitle());
        savedNews.setUrl(saveNewsRequest.getUrl());
        savedNews.setUrlToImage(saveNewsRequest.getUrlToImage());
        savedNews.setUser(user);

        savedNewsRepository.save(savedNews);
    }

    public Boolean isSaved(String title, Long userId){
        return savedNewsRepository.existsByTitleAndUserId(title,userId);
    }


    public void deleteNews(SaveNewsRequest saveNewsRequest, String accessToken) {
        
        savedNewsRepository.deleteByTitleAndUserId(saveNewsRequest.getTitle(), jwtUtil.extractUserId(accessToken));
    }



    

    
}
