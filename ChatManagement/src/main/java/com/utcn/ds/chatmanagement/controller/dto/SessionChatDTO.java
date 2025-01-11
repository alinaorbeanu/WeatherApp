package com.utcn.ds.chatmanagement.controller.dto;

import com.utcn.ds.chatmanagement.entity.Message;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionChatDTO {

    private Long id;

    private String status;

    private String client;

    private String admin;

    private List<MessageDTO> messages;
}
