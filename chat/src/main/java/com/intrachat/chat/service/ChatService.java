package com.intrachat.chat.service;

import com.intrachat.chat.dto.ChatDTO;
import com.intrachat.chat.dto.EmployeeRequestDTO;
import com.intrachat.chat.dto.EmployeeDTO;
import com.intrachat.chat.entity.ChatRoom;
import com.intrachat.chat.entity.Chat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ChatService {
    List<ChatRoom> getChatRoomsByEmployeeId(Long employeeId);

    Map<String, Object> login(EmployeeRequestDTO employeeRequestDTO);
    Flux<ChatDTO> findByRoomNum(Integer roomNum);
    Flux<ChatDTO> streamMessages(Integer roomNum);
    Mono<ChatDTO> setMsg(Chat chat);
    ChatRoom createRoom(Map<String, Object> requestData);
    List<EmployeeDTO> getAllEmployees();
    void inviteEmployeesToRoom(Map<String, Object> requestData);
    void leaveRoom(Map<String, Object> requestData);

    List<EmployeeDTO> getRoomMembers(Integer roomNum);
}
