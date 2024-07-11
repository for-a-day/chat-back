package com.intrachat.chat.service.impl;

import com.intrachat.chat.dto.ChatDTO;
import com.intrachat.chat.dto.EmployeeRequestDTO;
import com.intrachat.chat.dto.EmployeeDTO;
import com.intrachat.chat.entity.ChatRoom;
import com.intrachat.chat.entity.EmployeeEntity;
import com.intrachat.chat.entity.Chat;
import com.intrachat.chat.repository.ChatRoomRepository;
import com.intrachat.chat.repository.EmployeeRepository;
import com.intrachat.chat.repository.ChatRepository;
import com.intrachat.chat.service.ChatService;
import com.intrachat.chat.util.DtoConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DtoConverter dtoConverter;

    private final Sinks.Many<Chat> chatSink = Sinks.many().multicast().onBackpressureBuffer();

    public ChatServiceImpl() {
    }

    @Override
    public List<ChatRoom> getChatRoomsByEmployeeId(Long employeeId) {
        return chatRoomRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Map<String, Object> login(EmployeeRequestDTO employeeRequestDTO) {

        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findById(employeeRequestDTO.getEmployeeId());
        List<EmployeeEntity> employees = employeeRepository.findAll(); // 모든 직원 정보

        Map<String, Object> response = new HashMap<>();

        if (optionalEmployee.isPresent()) {
            EmployeeEntity employee = optionalEmployee.get();

            if (bCryptPasswordEncoder.matches(employeeRequestDTO.getEmployeePassword(),employee.getEmployeePassword())) {


                List<ChatRoom> chatRooms = chatRoomRepository.findByEmployeeId(employee.getEmployeeId());
                List<Map<String, Object>> chatRoomsWithNames = chatRooms.stream().map(chatRoom -> {
                    Map<String, Object> chatRoomData = new HashMap<>();
                    chatRoomData.put("roomNum", chatRoom.getRoomNum());
                    chatRoomData.put("roomName", chatRoom.getRoomName());
                    return chatRoomData;
                }).collect(Collectors.toList());

                response.put("employees", employees.stream().map(employeeEntity -> {
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    employeeDTO.setEmployeeId(employeeEntity.getEmployeeId());
                    employeeDTO.setName(employeeEntity.getName());
                    if (employeeEntity.getDepartment() != null) {
                        employeeDTO.setDepartmentName(employeeEntity.getDepartment().getDepartmentName());
                    }
                    if (employeeEntity.getLevel() != null) {
                        employeeDTO.setLevelName(employeeEntity.getLevel().getLevelName());
                    }
                    return employeeDTO;
                }).collect(Collectors.toList()));
                response.put("employeeId", employee.getEmployeeId());
                response.put("name", employee.getName());
                response.put("chatRooms", chatRoomsWithNames);
            } else {
                response.put("error", "Invalid password");
            }
        } else {
            response.put("error", "Employee not found");
        }

        return response;
    }

    @Override
    public Flux<ChatDTO> findByRoomNum(Integer roomNum) {
        return Flux.fromIterable(chatRepository.findByRoomNum(roomNum))
                .map(dtoConverter::convertToDto);
    }

    @Override
    public Flux<ChatDTO> streamMessages(Integer roomNum) {
        return chatSink.asFlux()
                .filter(chat -> chat.getRoomNum().equals(roomNum))
                .map(dtoConverter::convertToDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ChatDTO> setMsg(Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        EmployeeEntity employee = employeeRepository.findById(chat.getSender()).orElse(null);
        chat.setEmployee(employee);
        Chat savedChat = chatRepository.save(chat);
        chatSink.tryEmitNext(savedChat);

        return Mono.just(dtoConverter.convertToDto(savedChat));
    }

    @Override
    public ChatRoom createRoom(Map<String, Object> requestData) {
        String roomName = (String) requestData.get("roomName");
        List<Integer> employeeIds = (List<Integer>) requestData.get("employeeIds");

        int maxRoomNum = chatRoomRepository.findMaxRoomNum().orElse(0);
        int newRoomNum = maxRoomNum + 1;

        List<ChatRoom> createdRooms = new ArrayList<>();
        for (Integer empId : employeeIds) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setEmployeeId(empId.longValue());
            chatRoom.setRoomName(roomName);  // Ensure roomName is set
            chatRoom.setRoomNum(newRoomNum);
            createdRooms.add(chatRoomRepository.save(chatRoom));
        }

        return createdRooms.get(0); // Return only the newly created room
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeEntity> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> {
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    employeeDTO.setEmployeeId(employee.getEmployeeId());
                    employeeDTO.setName(employee.getName());
                    if (employee.getDepartment() != null) {
                        employeeDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
                    }
                    if (employee.getLevel() != null) {
                        employeeDTO.setLevelName(employee.getLevel().getLevelName());
                    }
                    return employeeDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void inviteEmployeesToRoom(Map<String, Object> requestData) {
        Integer roomNum = (Integer) requestData.get("roomNum");
        List<Integer> employeeIds = (List<Integer>) requestData.get("employeeIds");

        List<String> roomNames = chatRoomRepository.findRoomNamesByRoomNum(roomNum);
        String roomName = roomNames.isEmpty() ? null : roomNames.get(0);

        for (Integer empId : employeeIds) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setEmployeeId(empId.longValue());
            chatRoom.setRoomNum(roomNum);
            chatRoom.setRoomName(roomName);
            chatRoomRepository.save(chatRoom);
        }
    }



    @Override
    @Transactional
    public void leaveRoom(Map<String, Object> requestData) {
        Integer roomNum = (Integer) requestData.get("roomNum");
        Long employeeId = Long.valueOf((Integer) requestData.get("employeeId"));

        chatRoomRepository.deleteByRoomNumAndEmployeeId(roomNum, employeeId);
    }

    @Override
    public List<EmployeeDTO> getRoomMembers(Integer roomNum) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByRoomNum(roomNum);
        List<EmployeeDTO> members = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            EmployeeEntity employee = employeeRepository.findById(chatRoom.getEmployeeId()).orElse(null);
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
                members.add(employeeDTO);
            }
        }
        return members;
    }

}
