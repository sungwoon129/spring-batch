package com.blog.springbatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {
    private Long user_id;
    private String email;

    private String nickname;

    private String password;

    private LocalDateTime regDate;
}
