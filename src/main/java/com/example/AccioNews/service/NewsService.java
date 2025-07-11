package com.example.AccioNews.service;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.AccioNews.config.JwtUtil;
import com.example.AccioNews.dto.ArticleResponse;
import com.example.AccioNews.dto.NewsResponse;
import com.example.AccioNews.repository.SavedNewsRepository;

@Service
public class NewsService {

    // https://newsapi.org/v2/top-headlines?country=us&apiKey=602491e7d2d24f388e8955d57e4c721f

    private final RestTemplate restTemplate;

    @Value("${news.api.base-url}")
    private String baseUrl;

    @Value("${news.api.key}")
    private String apiKey;

    @Autowired
    private SavedNewsService savedNewsService;

    @Autowired
    private JwtUtil jwtUtil;

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    
    public NewsResponse getNewsByCountry(String country,String accessToken) {
    String url = prepareUrl(baseUrl, country, apiKey);
    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "Java/1.8.0");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<NewsResponse> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            NewsResponse.class);

    NewsResponse apiResponse = response.getBody();

    if (apiResponse == null || apiResponse.getArticles() == null) {
        return new NewsResponse(0, new ArrayList<>());
    }

    // Transform with isSaved flag
    List<ArticleResponse> updatedArticles = apiResponse.getArticles().stream()
            .map(article -> {
                boolean saved = savedNewsService.isSaved(article.getTitle(),jwtUtil.extractUserId(accessToken));
                return ArticleResponse.builder()
                        .author(article.getAuthor())
                        .title(article.getTitle())
                        .url(article.getUrl())
                        .urlToImage(article.getUrlToImage())
                        .content(article.getContent())
                        .isSaved(saved)
                        .build();
            })
            .collect(Collectors.toList());

    return new NewsResponse(apiResponse.getTotalResults(), new ArrayList<>(updatedArticles));
}

    private String prepareUrl(String baseUrl, String country, String apiKey) {
        return baseUrl + "?country=" + country + "&apiKey=" + apiKey;
    }
}
