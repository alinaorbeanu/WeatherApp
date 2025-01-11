package com.utcn.ds.chatmanagement.service.impl;

import com.utcn.ds.chatmanagement.controller.dto.MessageDTO;
import com.utcn.ds.chatmanagement.controller.dto.SeenRequestDTO;
import com.utcn.ds.chatmanagement.controller.dto.StatusDTO;
import com.utcn.ds.chatmanagement.controller.dto.WebSocketMsg;
import com.utcn.ds.chatmanagement.entity.Message;
import com.utcn.ds.chatmanagement.entity.SessionChat;
import com.utcn.ds.chatmanagement.exception.NotFoundObjectException;
import com.utcn.ds.chatmanagement.repository.MessageRepository;
import com.utcn.ds.chatmanagement.repository.SessionChatRepository;
import com.utcn.ds.chatmanagement.service.MessageService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final SessionChatRepository sessionChatRepository;

    private final ModelMapper modelMapper;

    private final SimpMessagingTemplate template;

    @Override
    public MessageDTO add(MessageDTO messageDTO) {
        messageDTO.setLocalDateTime(LocalDateTime.now().toString());
        Optional<SessionChat> sessionChat = this.sessionChatRepository.findById(Long.parseLong(messageDTO.getSession()));
        Message message = mapToMessage(messageDTO);
        if (sessionChat.isPresent()) {
            message.setSession(sessionChat.get());
            this.messageRepository.save(message);
            WebSocketMsg webSocketMsg;
            if (message.getSender().equals(sessionChat.get().getAdmin())) {
                webSocketMsg = WebSocketMsg.builder()
                        .message(message.getContent())
                        .userName(sessionChat.get().getClient())
                        .sender(message.getSender())
                        .build();
            } else {
                webSocketMsg = WebSocketMsg.builder()
                        .message(message.getContent())
                        .userName(sessionChat.get().getAdmin())
                        .sender(message.getSender())
                        .build();
            }

            this.template.convertAndSend("/topic", webSocketMsg);

        } else
            throw new NotFoundObjectException("Session Chat with id: " + messageDTO.getSession() + " was not found!");
        return mapToMessageDTO(message);
    }

    @Override
    public List<MessageDTO> findAll() {
        return null;
    }

    @Override
    public List<Long> findAllUnreadMessagesByChatsAndUsername(List<Long> chats, String username) {
        return chats.stream()
                .map(this.sessionChatRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(sessionChat -> messageRepository.findAllByStatusAndSession("unseen", sessionChat).stream())
                .filter(message -> !message.getSender().equals(username))
                .map(Message::getId)
                .collect(toList());
    }

    @Override
    public MessageDTO findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public StatusDTO updateMessageStatus(SeenRequestDTO seenRequestDTO) {
        Optional<SessionChat> sessionChat = sessionChatRepository.findById(seenRequestDTO.getId());
        if (sessionChat.isPresent()) {
            List<Message> messages = this.messageRepository.findAllBySenderAndSession(seenRequestDTO.getUser(), sessionChat.get());

            for (Message message : messages) {
                message.setStatus(("seen"));
                this.messageRepository.save(message);
            }
            WebSocketMsg webSocketMsg;

            webSocketMsg = WebSocketMsg.builder()
                    .message("seen")
                    .userName(seenRequestDTO.getUser())
                    .sender(sessionChat.get().getClient())
                    .build();

            this.template.convertAndSend("/topic3", webSocketMsg);

        } else throw new NotFoundObjectException("Session Chat with id: " + seenRequestDTO.getId() + " was not found!");

        return StatusDTO.builder().status("Success!").build();
    }

    @Override
    public MessageDTO isTypingNotification(MessageDTO messageDTO) {
        messageDTO.setLocalDateTime(LocalDateTime.now().toString());
        Optional<SessionChat> sessionChat = this.sessionChatRepository.findById(Long.parseLong(messageDTO.getSession()));
        Message message = mapToMessage(messageDTO);

        if (sessionChat.isPresent()) {
            message.setSession(sessionChat.get());

            WebSocketMsg webSocketMsg;
            if (message.getSender().equals(sessionChat.get().getAdmin())) {
                webSocketMsg = WebSocketMsg.builder()
                        .message(message.getContent())
                        .userName(sessionChat.get().getClient())
                        .sender(message.getSender())
                        .build();
            } else {
                webSocketMsg = WebSocketMsg.builder()
                        .message(message.getContent())
                        .userName(sessionChat.get().getAdmin())
                        .sender(message.getSender())
                        .build();
            }

            this.template.convertAndSend("/topic2", webSocketMsg);

        } else
            throw new NotFoundObjectException("Session Chat with id: " + messageDTO.getSession() + " was not found!");
        return mapToMessageDTO(message);
    }

    public Message mapToMessage(MessageDTO messageDTO) {
        modelMapper.addConverter(ctx -> ctx.getSource() != null ? LocalDateTime.parse(ctx.getSource(), DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null,
                String.class, LocalDateTime.class);

        return modelMapper.map(messageDTO, Message.class);
    }

    private MessageDTO mapToMessageDTO(Message message) {
        return modelMapper.map(message, MessageDTO.class);
    }
}
