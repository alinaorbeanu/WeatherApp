package com.utcn.ds.chatmanagement.controller.impl;

import com.utcn.ds.chatmanagement.controller.SessionChatController;
import com.utcn.ds.chatmanagement.controller.dto.SessionChatDTO;
import com.utcn.ds.chatmanagement.controller.dto.UserDTO;
import com.utcn.ds.chatmanagement.service.SessionChatService;
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
public class SessionChatControllerImpl implements SessionChatController {

    private final SessionChatService sessionChatService;

    @Override
    public ResponseEntity<SessionChatDTO> add(SessionChatDTO sessionChatDTO) {
        return new ResponseEntity<>(sessionChatService.add(sessionChatDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SessionChatDTO> findById(Long id) {
        return new ResponseEntity<>(sessionChatService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<SessionChatDTO>> findAll(HttpServletRequest request) {
        return new ResponseEntity<>(sessionChatService.findAll(request), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SessionChatDTO> findAllByClientAndAdminAndStatus(HttpServletRequest request, String client, String admin) {
        return new ResponseEntity<>(sessionChatService.findAllByClientAndAdminAndStatus(request, client, admin), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAllByClientOrAdminAndStatus(HttpServletRequest request, String username) {
        return new ResponseEntity<>(sessionChatService.findAllByClientOrAdminAndStatus(request, username), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Long>> findAllOpenedChatsByUsernameAndStatus(HttpServletRequest request, String username) {
        return new ResponseEntity<>(sessionChatService.findAllOpenedChatsByUsernameAndStatus(request, username), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        sessionChatService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<SessionChatDTO> closeSessionChat(Long id) {
        return new ResponseEntity<>(sessionChatService.closeSessionChat(id), HttpStatus.OK);
    }
}
