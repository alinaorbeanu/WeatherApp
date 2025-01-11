package com.utcn.ds.chatmanagement.controller;

import com.utcn.ds.chatmanagement.controller.dto.SessionChatDTO;
import com.utcn.ds.chatmanagement.controller.dto.UserDTO;
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

public interface SessionChatController {

    @PostMapping(value = "/session/chat")
    ResponseEntity<SessionChatDTO> add(@RequestBody SessionChatDTO sessionChatDTO);

    @GetMapping(value = "/session/chat/{id}")
    @ResponseBody
    ResponseEntity<SessionChatDTO> findById(@PathVariable Long id);

    @GetMapping(value = "/session/chat")
    ResponseEntity<List<SessionChatDTO>> findAll(HttpServletRequest request);

    @GetMapping(value = "/session/chat/{client}/{admin}")
    ResponseEntity<SessionChatDTO> findAllByClientAndAdminAndStatus(HttpServletRequest request,
                                                                    @PathVariable String client,
                                                                    @PathVariable String admin);

    @GetMapping(value = "/session/chat/username/{username}")
    ResponseEntity<List<UserDTO>> findAllByClientOrAdminAndStatus(HttpServletRequest request,
                                                                  @PathVariable String username);

    @GetMapping(value = "/session/chat/open/{username}")
    ResponseEntity<List<Long>> findAllOpenedChatsByUsernameAndStatus(HttpServletRequest request,
                                                                     @PathVariable String username);

    @DeleteMapping(value = "/session/chat/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @PutMapping(value = "/session/chat/close")
    ResponseEntity<SessionChatDTO> closeSessionChat(@RequestBody(required = false) Long id);
}

