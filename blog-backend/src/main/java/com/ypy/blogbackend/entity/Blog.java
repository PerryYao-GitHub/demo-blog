package com.ypy.blogbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("t_blog")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer authId;

    private String authAccount;

    private String title;  // 用户可改

    private String description;  // 用户可改

    private String content;  // 用户可改

    private Byte permission;  // 用户可改

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Byte deleted;
}
