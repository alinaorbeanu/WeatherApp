package com.utcn.ds.chatmanagement.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMsg {

    private String message;
    private String userName;
    private String sender;
}
