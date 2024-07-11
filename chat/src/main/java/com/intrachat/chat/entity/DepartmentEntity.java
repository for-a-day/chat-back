package com.intrachat.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "DEPARTMENT")
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity {

    @Id
    @Column(name = "DEPARTMENT_CODE")
    private Long departmentCode;

    @Column(name = "DEPARTMENT_NAME", nullable = false)
    private String departmentName;
}