package com.example.AccioNews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AccioNews.model.RefreshToken;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, Long>{

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken findByToken(String refreshToken);

    void delete(RefreshToken token);

    RefreshToken getByUserId(Long id);

    

}
