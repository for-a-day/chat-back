package com.intrachat.chat.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatDTO {
    private Long id;
    private String msg;
    private Long sender;
    private String senderName;
    private Integer roomNum;
    private LocalDateTime createdAt;
    private EmployeeDTO employee;
}
