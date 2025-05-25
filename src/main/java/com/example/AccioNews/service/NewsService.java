package com.example.AccioNews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.AccioNews.dto.NewsResponse;

@Service
public class NewsService {

    //https://newsapi.org/v2/top-headlines?country=us&apiKey=602491e7d2d24f388e8955d57e4c721f

    @Autowired
    RestTemplate restTemplate;

    String baseUrl = "https://newsapi.org/v2/top-headlines";

    public NewsResponse getNewsByCountry(String country, String apiKey){
        String url = prepareUrl(baseUrl,country,apiKey);
        NewsResponse response = restTemplate.getForObject(url,NewsResponse.class);
        return response;
    }
        
    private String prepareUrl(String baseUrl2, String country, String apiKey) {
                
        return baseUrl2 + "?country="+country+"&apiKey="+apiKey;
    }

}
