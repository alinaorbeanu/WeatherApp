package com.utcn.ds.chatmanagement.repository;

import com.utcn.ds.chatmanagement.entity.Message;
import com.utcn.ds.chatmanagement.entity.SessionChat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySenderAndSession(String sender, SessionChat session);

    List<Message> findAllByStatusAndSession(String status, SessionChat sessionChat);
}
