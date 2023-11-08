package com.blog.springbatch;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "users")
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private LocalDateTime regDate;

}