package com.utcn.ds.chatmanagement.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sessionchat")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String status;

    @Column(
            name = "client",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String client;

    @Column(
            name = "admin",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String admin;

    @OneToMany(mappedBy = "session",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Message> messages;
}
