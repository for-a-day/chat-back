package com.intrachat.chat.repository;

import com.intrachat.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByEmployeeId(Long employeeId);

    @Query("SELECT MAX(cr.roomNum) FROM ChatRoom cr")
    Optional<Integer> findMaxRoomNum();



    void deleteByRoomNumAndEmployeeId(Integer roomNum, Long employeeId);

    List<ChatRoom> findByRoomNum(Integer roomNum);


    @Query("SELECT cr.roomName FROM ChatRoom cr WHERE cr.roomNum = :roomNum")
    List<String> findRoomNamesByRoomNum(@Param("roomNum") Integer roomNum);



}
