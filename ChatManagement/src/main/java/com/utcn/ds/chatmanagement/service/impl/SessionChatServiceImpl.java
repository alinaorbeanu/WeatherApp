package com.utcn.ds.chatmanagement.service.impl;

import com.utcn.ds.chatmanagement.controller.dto.MessageDTO;
import com.utcn.ds.chatmanagement.controller.dto.SessionChatDTO;
import com.utcn.ds.chatmanagement.controller.dto.UserDTO;
import com.utcn.ds.chatmanagement.entity.SessionChat;
import com.utcn.ds.chatmanagement.entity.User;
import com.utcn.ds.chatmanagement.exception.NotFoundObjectException;
import com.utcn.ds.chatmanagement.repository.SessionChatRepository;
import com.utcn.ds.chatmanagement.repository.UserRepository;
import com.utcn.ds.chatmanagement.service.MessageService;
import com.utcn.ds.chatmanagement.service.SessionChatService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionChatServiceImpl implements SessionChatService {

    private final SessionChatRepository sessionChatRepository;

    private final MessageService messageService;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public SessionChatDTO add(SessionChatDTO sessionChatDTO) {
        List<User> users = userRepository.findAllByRole("ADMIN");
        SessionChat sessionChat = mapToSessionChat(sessionChatDTO);
        Random random = new Random();

        int randomIndex = random.nextInt(users.size());
        User randomAdmin = users.get(randomIndex);

        sessionChat.setAdmin(randomAdmin.getEmail());
        sessionChat.setStatus("activ");

        Optional<SessionChat> sessionChatAsOptional = sessionChatRepository.findAllByClientAndAdminAndStatus(sessionChat.getClient(), sessionChat.getAdmin(), "activ");

        if (sessionChatAsOptional.isPresent()) {
            sessionChatAsOptional.get().setStatus("inactiv");
            sessionChatRepository.save(sessionChatAsOptional.get());
            sessionChatRepository.save(sessionChat);
        } else {
            sessionChatRepository.save(sessionChat);
        }

        SessionChatDTO savedSessionChatDTO = mapToSessionChatDTO(sessionChat);

        MessageDTO messageDTO = MessageDTO.builder()
                .content("Buna! Cu ce te pot ajuta?")
                .sender(randomAdmin.getEmail())
                .status("unseen")
                .session(savedSessionChatDTO.getId().toString())
                .build();

        messageService.add(messageDTO);

        return savedSessionChatDTO;
    }

    @Override
    public List<SessionChatDTO> findAll(HttpServletRequest request) {
        return null;
    }

    @Override
    public SessionChatDTO findAllByClientAndAdminAndStatus(HttpServletRequest request, String client, String admin) {
        Optional<SessionChat> sessionChat = sessionChatRepository.findAllByClientAndAdminAndStatus(client, admin, "activ");
        if (sessionChat.isPresent()) {
            return mapToSessionChatDTO(sessionChat.get());
        } else {
            Optional<SessionChat> sessionChatReverse = sessionChatRepository.findAllByClientAndAdminAndStatus(admin, client, "activ");
            if (sessionChatReverse.isPresent()) {
                return mapToSessionChatDTO(sessionChatReverse.get());
            }
        }
        return new SessionChatDTO();
    }

    @Override
    public List<UserDTO> findAllByClientOrAdminAndStatus(HttpServletRequest request, String username) {
        List<UserDTO> users = new ArrayList<>();
        List<SessionChat> sessionChats = this.sessionChatRepository.findAllByStatusAndClientOrAdmin("activ", username, username);
        List<SessionChat> filteredList = sessionChats.stream()
                .filter(session -> session.getStatus().equals("activ"))
                .toList();

        for (SessionChat sessionChat : filteredList) {
            if (sessionChat.getAdmin().equals(username)) {
                Optional<User> user = userRepository.findByEmail(sessionChat.getClient());
                users.add(mapToUserDTO(user.get()));
            } else {
                Optional<User> user = userRepository.findByEmail(sessionChat.getAdmin());
                users.add(mapToUserDTO(user.get()));
            }
        }
        return users;

    }

    @Override
    public List<Long> findAllOpenedChatsByUsernameAndStatus(HttpServletRequest request, String username) {
        List<Long> chats = new ArrayList<>();

        List<SessionChat> sessionChats = this.sessionChatRepository.findAllByStatusAndClientOrAdmin("activ", username, username);

        for (SessionChat sessionChat : sessionChats) {
            chats.add(sessionChat.getId());
        }

        return chats;
    }

    @Override
    public SessionChatDTO findById(Long id) {
        return sessionChatRepository.findById(id)
                .map(this::mapToSessionChatDTO)
                .orElseThrow(() -> new NotFoundObjectException("Session with id: " + id + " not found!"));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public SessionChatDTO closeSessionChat(Long id) {
        Optional<SessionChat> sessionChat = sessionChatRepository.findById(id);
        if (sessionChat.isPresent()) {
            sessionChat.get().setStatus("inactiv");
            this.sessionChatRepository.save(sessionChat.get());
        }

        return mapToSessionChatDTO(sessionChat.get());
    }

    private SessionChatDTO mapToSessionChatDTO(SessionChat sessionChat) {
        return modelMapper.map(sessionChat, SessionChatDTO.class);
    }

    private SessionChat mapToSessionChat(SessionChatDTO sessionChatDTO) {
        return modelMapper.map(sessionChatDTO, SessionChat.class);
    }

    private UserDTO mapToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
