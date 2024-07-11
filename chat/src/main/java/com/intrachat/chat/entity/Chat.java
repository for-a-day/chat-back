package com.intrachat.chat.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CHAT")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MSG")
    private String msg;

    @Column(name = "SENDER")
    private Long sender; // 보내는 사람

    @Column(name = "SENDER_NAME")
    private String senderName;

    @Column(name = "ROOM_NUM")
    private Integer roomNum; // 방 번호

    @Column(name = "CREATE_AT")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER", insertable = false, updatable = false)
    private EmployeeEntity employee;
}
