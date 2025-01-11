package com.utcn.ds.chatmanagement.controller;

import com.utcn.ds.chatmanagement.controller.dto.MessageDTO;
import com.utcn.ds.chatmanagement.controller.dto.SeenRequestDTO;
import com.utcn.ds.chatmanagement.controller.dto.StatusDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface MessageController {

    @PostMapping(value = "/message")
    ResponseEntity<MessageDTO> add(@RequestBody MessageDTO messageDTO);

    @GetMapping(value = "/message/{id}")
    @ResponseBody
    ResponseEntity<MessageDTO> findById(@PathVariable Long id);

    @GetMapping(value = "/message")
    ResponseEntity<List<MessageDTO>> findAll();

    @GetMapping(value = "/message/unread/{chats}/{username}")
    ResponseEntity<List<Long>> findAllUnreadMessagesByChatsAndUsername(HttpServletRequest request,
                                                                       @PathVariable List<Long> chats,
                                                                       @PathVariable String username);

    @DeleteMapping(value = "/message/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @PutMapping(value = "/message")
    ResponseEntity<StatusDTO> updateMessageStatus(@RequestBody(required = false) SeenRequestDTO seenRequestDTO);

    @PostMapping(value = "/message/isTyping")
    ResponseEntity<MessageDTO> isTypingNotification(@RequestBody MessageDTO messageDTO);
}
