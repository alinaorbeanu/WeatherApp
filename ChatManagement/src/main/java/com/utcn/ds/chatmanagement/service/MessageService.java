package com.utcn.ds.chatmanagement.service;

import com.utcn.ds.chatmanagement.controller.dto.MessageDTO;
import com.utcn.ds.chatmanagement.controller.dto.SeenRequestDTO;
import com.utcn.ds.chatmanagement.controller.dto.StatusDTO;
import java.util.List;

public interface MessageService {

    MessageDTO add(MessageDTO messageDTO);

    List<MessageDTO> findAll();

    List<Long> findAllUnreadMessagesByChatsAndUsername(List<Long> chats, String username);

    MessageDTO findById(Long id);

    void delete(Long id);

    StatusDTO updateMessageStatus(SeenRequestDTO seenRequestDTO);

    MessageDTO isTypingNotification(MessageDTO messageDTO);
}
