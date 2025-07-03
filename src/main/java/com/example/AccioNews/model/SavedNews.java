package com.example.AccioNews.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saved_news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedNews {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "url_to_image")
    private String urlToImage;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

}
