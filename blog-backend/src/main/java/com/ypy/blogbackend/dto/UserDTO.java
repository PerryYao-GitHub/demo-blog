package com.ypy.blogbackend.dto;

import com.ypy.blogbackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    private String account;

    private String name; // 可改

    private String avatar;  // 可改

    private String phone;  // 可改

    private String email;  // 可改

    private Byte status;

    private LocalDateTime loginTime;

    private LocalDateTime createTime;

    private Integer blogCnt;

    private Integer followedCnt;

    private Integer followerCnt;

    private Boolean isFollowed;  // ************

    private LocalDateTime followedTime;  // ************

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setAccount(user.getAccount());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setLoginTime(user.getLoginTime());
        dto.setCreateTime(user.getCreateTime());
        dto.setBlogCnt(user.getBlogCnt());
        dto.setFollowedCnt(user.getFollowedCnt());
        dto.setFollowerCnt(user.getFollowerCnt());
        return dto;
    }

    public static UserDTO toOthersDTO(User user, Boolean isFollowed, LocalDateTime followedTime) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setAccount(user.getAccount());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setStatus(user.getStatus());
        dto.setCreateTime(user.getCreateTime());
        dto.setBlogCnt(user.getBlogCnt());
        dto.setFollowedCnt(user.getFollowedCnt());
        dto.setFollowerCnt(user.getFollowerCnt());
        dto.setIsFollowed(isFollowed);
        dto.setFollowedTime(followedTime);
        return dto;
    }
}
