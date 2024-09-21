package com.ypy.blogbackend.dto;

import com.ypy.blogbackend.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDTO {
    private Integer id;

    private Integer authId;

    private String authAccount;

    private String authName; // *********

    private String authAvatar; // *********

    private String title;  // 用户可改

    private String description;  // 用户可改

    private String content;  // 用户可改

    private Byte permission;  // 用户可改

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static BlogDTO toDTO(Blog blog) {
        BlogDTO dto = new BlogDTO();
        dto.setId(blog.getId());
        dto.setAuthId(blog.getAuthId());
        dto.setAuthAccount(blog.getAuthAccount());
        dto.setTitle(blog.getTitle());
        dto.setDescription(blog.getDescription());
        dto.setContent(blog.getContent());
        dto.setPermission(blog.getPermission());
        dto.setCreateTime(blog.getCreateTime());
        dto.setUpdateTime(blog.getUpdateTime());
        return dto;
    }
}
