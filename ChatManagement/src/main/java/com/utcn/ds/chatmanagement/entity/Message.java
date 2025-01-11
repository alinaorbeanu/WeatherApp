package com.utcn.ds.chatmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "message")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "content",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Column(
            name = "sender",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String sender;

    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String status;

    @Column(
            name = "localDateTime",
            nullable = false
    )
    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private SessionChat session;
}
