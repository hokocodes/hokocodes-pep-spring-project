package com.example.repository;

import com.example.entity.Message;
import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByPostedBy(Integer account);

    @Query("SELECT m FROM Message m WHERE m.postedBy = :userId")
    List<Message> findMessagesByUserId(@Param("userId") Integer userId);
}