package com.utcn.ds.chatmanagement.controller.impl;

import com.utcn.ds.chatmanagement.controller.MessageController;
import com.utcn.ds.chatmanagement.controller.dto.MessageDTO;
import com.utcn.ds.chatmanagement.controller.dto.SeenRequestDTO;
import com.utcn.ds.chatmanagement.controller.dto.StatusDTO;
import com.utcn.ds.chatmanagement.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MessageControllerImpl implements MessageController {

    private final MessageService messageService;

    @Override
    public ResponseEntity<MessageDTO> add(MessageDTO messageDTO) {
        return new ResponseEntity<>(messageService.add(messageDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MessageDTO> findById(Long id) {
        return new ResponseEntity<>(messageService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> findAll() {
        return new ResponseEntity<>(messageService.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Long>> findAllUnreadMessagesByChatsAndUsername(HttpServletRequest request, List<Long> chats, String username) {
        return new ResponseEntity<>(messageService.findAllUnreadMessagesByChatsAndUsername(chats, username), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        messageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<StatusDTO> updateMessageStatus(SeenRequestDTO seenRequestDTO) {
        return new ResponseEntity<>(messageService.updateMessageStatus(seenRequestDTO), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MessageDTO> isTypingNotification(MessageDTO messageDTO) {
        return new ResponseEntity<>(messageService.isTypingNotification(messageDTO), HttpStatus.OK);
    }
}
