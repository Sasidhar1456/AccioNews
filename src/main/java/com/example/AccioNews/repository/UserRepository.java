package com.example.AccioNews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AccioNews.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    User save(User user);

    User findByEmail(String email);

}
