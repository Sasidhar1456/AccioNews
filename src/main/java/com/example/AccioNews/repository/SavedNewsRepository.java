package com.example.AccioNews.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.AccioNews.model.SavedNews;



@Repository
public interface SavedNewsRepository extends JpaRepository<SavedNews, Long> {
    List<SavedNews> findByUserId(Long userId);

    boolean existsByTitle(String title);

    @Query("SELECT CASE WHEN COUNT(sn) > 0 THEN true ELSE false END FROM SavedNews sn WHERE sn.title = :title AND sn.user.id = :userId")
    boolean existsByTitleAndUserId(@Param("title") String title, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SavedNews sn WHERE sn.title = :title AND sn.user.id = :userId")
    void deleteByTitleAndUserId(@Param("title") String title, @Param("userId") Long userId);

}
