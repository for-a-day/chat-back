package com.intrachat.chat.util;

import org.springframework.stereotype.Component;
import com.intrachat.chat.dto.ChatDTO;
import com.intrachat.chat.dto.EmployeeDTO;
import com.intrachat.chat.entity.Chat;
import com.intrachat.chat.entity.EmployeeEntity;

@Component
public class DtoConverter {

    public ChatDTO convertToDto(Chat chat) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setId(chat.getId());
        chatDTO.setMsg(chat.getMsg());
        chatDTO.setSender(chat.getSender());
        chatDTO.setSenderName(chat.getSenderName());
        chatDTO.setRoomNum(chat.getRoomNum());
        chatDTO.setCreatedAt(chat.getCreatedAt());

        EmployeeEntity employee = chat.getEmployee();
        if (employee != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setEmployeeId(employee.getEmployeeId());
            employeeDTO.setName(employee.getName());
            if (employee.getDepartment() != null) {
                employeeDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
            }
            if (employee.getLevel() != null) {
                employeeDTO.setLevelName(employee.getLevel().getLevelName());
            }
            chatDTO.setEmployee(employeeDTO);
        }

        return chatDTO;
    }
}
