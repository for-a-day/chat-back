package com.intrachat.chat.repository;

import com.intrachat.chat.dto.EmployeeRequestDTO;
import com.intrachat.chat.entity.EmployeeEntity;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

}
