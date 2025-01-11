package com.utcn.ds.chatmanagement.service;

import com.utcn.ds.chatmanagement.controller.dto.SessionChatDTO;
import com.utcn.ds.chatmanagement.controller.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface SessionChatService {

    SessionChatDTO add(SessionChatDTO sessionChatDTO);

    List<SessionChatDTO> findAll(HttpServletRequest request);

    SessionChatDTO findAllByClientAndAdminAndStatus(HttpServletRequest request, String client, String admin);

    List<UserDTO> findAllByClientOrAdminAndStatus(HttpServletRequest request, String username);

    List<Long> findAllOpenedChatsByUsernameAndStatus(HttpServletRequest request, String username);

    SessionChatDTO findById(Long id);

    void delete(Long id);

    SessionChatDTO closeSessionChat(Long id);
}