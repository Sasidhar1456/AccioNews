package com.example.AccioNews.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.AccioNews.dto.NewsResponse;

@Service
public class NewsService {

    // https://newsapi.org/v2/top-headlines?country=us&apiKey=602491e7d2d24f388e8955d57e4c721f

    private final RestTemplate restTemplate;

    @Value("${news.api.base-url}")
    private String baseUrl;

    @Value("${news.api.key}")
    private String apiKey;

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NewsResponse getNewsByCountry(String country) {
        String url = prepareUrl(baseUrl, country, apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Java/1.8.0");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<NewsResponse> response = restTemplate.exchange(
                url, // your full NewsAPI URL with API key
                HttpMethod.GET,
                entity,
                NewsResponse.class);

        return response.getBody();
    }

    private String prepareUrl(String baseUrl, String country, String apiKey) {
        return baseUrl + "?country=" + country + "&apiKey=" + apiKey;
    }
}
