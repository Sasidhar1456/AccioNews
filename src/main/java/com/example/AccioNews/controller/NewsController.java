package com.example.AccioNews.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AccioNews.dto.NewsResponse;
import com.example.AccioNews.service.NewsService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/news")
public class NewsController{

    @Autowired
    NewsService newsService;

    @GetMapping("/country")
    public NewsResponse getNewsByCountry(@RequestParam("country") String country, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;

        

        return newsService.getNewsByCountry(country, accessToken);
    }

}