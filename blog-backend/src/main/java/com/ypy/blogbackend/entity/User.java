package com.ypy.blogbackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String account;

    @JsonIgnore
    private String password;

    private String name;

    private String avatar;

    @JsonIgnore
    private String phone;

    @JsonIgnore
    private String email;

    private Byte status;

    private Byte role;

    @JsonIgnore
    private LocalDateTime loginTime;

    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    @TableLogic
    private Byte deleted;

    private Integer blogCnt;

    private Integer followedCnt;

    private Integer followerCnt;

    private static final long serialVersionUID = 1L;
}
