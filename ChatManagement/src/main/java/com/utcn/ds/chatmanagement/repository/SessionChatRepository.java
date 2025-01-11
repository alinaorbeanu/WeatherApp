package com.utcn.ds.chatmanagement.repository;

import com.utcn.ds.chatmanagement.entity.SessionChat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionChatRepository extends JpaRepository<SessionChat, Long> {

    Optional<SessionChat> findAllByClientAndAdminAndStatus(String client, String admin, String status);

    List<SessionChat> findAllByStatusAndClientOrAdmin(String status, String client, String admin);
}
