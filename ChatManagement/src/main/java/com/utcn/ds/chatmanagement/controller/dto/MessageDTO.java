package com.utcn.ds.chatmanagement.controller.dto;

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
public class MessageDTO {

    private Long id;

    private String content;

    private String sender;

    private String status;

    private String localDateTime;

    private String session;
}
