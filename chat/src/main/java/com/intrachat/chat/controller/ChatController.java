package com.intrachat.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

import com.intrachat.chat.dto.ChatDTO;
import com.intrachat.chat.dto.EmployeeRequestDTO;
import com.intrachat.chat.dto.EmployeeDTO;
import com.intrachat.chat.entity.ChatRoom;
import com.intrachat.chat.entity.Chat;
import com.intrachat.chat.service.ChatService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody EmployeeRequestDTO employeeRequestDTO){
        Map<String, Object> response = chatService.login(employeeRequestDTO);
        if (response.containsKey("error")) {
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/list")
    public ResponseEntity<List<ChatRoom>> getChatRoomsByEmployeeId(@RequestBody Map<String, Object> requestData) {
        Long employeeId = Long.valueOf((Integer) requestData.get("employeeId"));
        List<ChatRoom> chatRooms = chatService.getChatRoomsByEmployeeId(employeeId);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/roomNum/{roomNum}")
    public Flux<ChatDTO> findByRoomNum(@PathVariable Integer roomNum) {
        return chatService.findByRoomNum(roomNum);
    }

    @GetMapping("/stream/roomNum/{roomNum}")
    public Flux<ChatDTO> streamMessages(@PathVariable Integer roomNum) {
        return chatService.streamMessages(roomNum);
    }

    @PostMapping
    public Mono<ChatDTO> setMsg(@RequestBody Chat chat) {
        return chatService.setMsg(chat);
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody Map<String, Object> requestData) {
        ChatRoom createdRoom = chatService.createRoom(requestData);
        return ResponseEntity.ok(createdRoom);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = chatService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/invite")
    public ResponseEntity<Void> inviteEmployeesToRoom(@RequestBody Map<String, Object> requestData) {
        chatService.inviteEmployeesToRoom(requestData);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leaveRoom(@RequestBody Map<String, Object> requestData) {
        chatService.leaveRoom(requestData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomNum}/members")
    public ResponseEntity<List<EmployeeDTO>> getRoomMembers(@PathVariable Integer roomNum) {
        List<EmployeeDTO> members = chatService.getRoomMembers(roomNum);
        return ResponseEntity.ok(members);
    }

}
