package com.example.AccioNews.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.AccioNews.config.JwtUtil;
import com.example.AccioNews.dto.ArticleResponse;
import com.example.AccioNews.dto.SaveNewsRequest;
import com.example.AccioNews.service.SavedNewsService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/saved-news/v1")
public class SavedNewsController {

    @Autowired
    private SavedNewsService savedNewsService;

    

     @GetMapping("/getSavedNews")
     public ResponseEntity<?> getSavedNews(HttpServletRequest request) {
         String authHeader = request.getHeader("Authorization");
         String accessToken = authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;

        try{
            List<ArticleResponse> response = savedNewsService.getSavedNews(accessToken);
            
            
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body("Failed to delete try again");
        }
        
    }

    @PostMapping("/saveNews")
    public ResponseEntity<?> saveNews(@RequestBody SaveNewsRequest saveNewsRequest, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;

        try{
            savedNewsService.saveNews(saveNewsRequest,accessToken);
            Map<String, String> response = new HashMap<>();
            response.put("message", "News saved successfully!");
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body("Failed to delete try again");
        }
    }

    @PostMapping("/deleteNews")
    public ResponseEntity<?> deleteNews(@RequestBody SaveNewsRequest saveNewsRequest, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader != null && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : null;

        try{
            savedNewsService.deleteNews(saveNewsRequest,accessToken);
            Map<String, String> response = new HashMap<>();
            response.put("message", "News Deleted successfully!");
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body("Failed to delete try again");
        }
    }

    
}
