package com.intrachat.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Table(name = "EMPLOYEE_LEVEL")
@AllArgsConstructor
@NoArgsConstructor
public class LevelEntity {

    @Id
    @Column(name = "LEVEL_CODE")
    private Long levelCode;

    @Column(name = "LEVEL_NAME", nullable = false)
    private String levelName;
}