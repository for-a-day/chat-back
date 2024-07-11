package com.intrachat.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.intrachat.chat.entity.Chat;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByRoomNum(Integer roomNum);
}
