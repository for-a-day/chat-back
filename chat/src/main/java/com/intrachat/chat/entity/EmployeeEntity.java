package com.intrachat.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Table(name = "EMPLOYEE")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEntity {

    //아이디
    @Id
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    //패스워드
    @Column(name = "EMPLOYEE_PASSWORD", nullable = false)
    private String employeePassword;

    //이름
    @Column(name = "NAME", nullable = false)
    private String name;

    //성별
    @Column(name = "GENDER", nullable = false)
    private String gender;

    //생년월일
    @Column(name = "BIRTH", nullable = false)
    private Date birth;

    //입사날짜
    @Column(name = "DATE_EMPLOYMENT", nullable = false)
    private Date dateEmployment;

    //연락처
    @Column(name = "CONTACT", nullable = false)
    private String contact;

    //주소
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    //이메일주소
    @Column(name = "EMAIL_ADDRESS", unique = true, nullable = false)
    private String emailAddress;

    //재직여부
    @Column(name = "EMPLOYMENT_STATUS", nullable = false)
    private String employmentStatus;

    @ManyToOne
    @JoinColumn(name = "LEVEL_CODE")
    private LevelEntity level;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_CODE")
    private DepartmentEntity department;

    @ManyToOne
    @JoinColumn(name = "AUTHORITY_CODE")
    private AuthorityEntity authority;
}
