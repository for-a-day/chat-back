package com.intrachat.chat.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateRoomRequestDTO {
    private String roomName;
    private List<Long> employeeIds;
}
