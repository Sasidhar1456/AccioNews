package com.example.AccioNews.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AccioNews.dto.NewsResponse;
import com.example.AccioNews.service.NewsService;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController{

    @Autowired
    NewsService newsService;

    @GetMapping("/country")
    public NewsResponse getNewsByCountry(@RequestParam("country") String country) {
        return newsService.getNewsByCountry(country);
    }

}