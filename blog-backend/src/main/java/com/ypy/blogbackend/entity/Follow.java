package com.ypy.blogbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("r_follow")
public class Follow {
    @TableId(type = IdType.AUTO)
    Integer id;

    Integer followerId;

    Integer followedId;

    Byte isValid;  // 0 or 1

    LocalDateTime createTime;
}
