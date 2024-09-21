package com.ypy.blogbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ypy.blogbackend.common.Code;
import com.ypy.blogbackend.common.Resp;
import com.ypy.blogbackend.common.UserStatus;
import com.ypy.blogbackend.common.exception.BusinessException;
import com.ypy.blogbackend.dto.UserDTO;
import com.ypy.blogbackend.entity.Follow;
import com.ypy.blogbackend.entity.User;
import com.ypy.blogbackend.mapper.FollowMapper;
import com.ypy.blogbackend.mapper.UserMapper;
import com.ypy.blogbackend.service.UserAccountService;
import com.ypy.blogbackend.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {
    @Resource
    private UserAccountService userAccountService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public Resp checkOneUser(Integer visitorId, Integer targetId) {
        if (visitorId == null || targetId == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (visitorId.equals(targetId)) return userAccountService.checkAccount(visitorId);
        // 如果是别人, 要返回关注信息
        User user = userMapper.selectById(targetId);
        if (user == null) throw new BusinessException(Code.ERROR_PARAMS_INVALID);

        QueryWrapper<Follow> qw = new QueryWrapper<>();
        qw.eq("follower_id", visitorId);
        qw.eq("followed_id", targetId);
        qw.eq("is_valid", 1);
        Follow f = followMapper.selectOne(qw);

        Boolean isFollowed = null;
        LocalDateTime followedTime = null;
        if (f == null) isFollowed = false;
        else {
            isFollowed = true;
            followedTime = LocalDateTime.now();
        }
        UserDTO userDTO = UserDTO.toOthersDTO(user, isFollowed, followedTime);
        return Resp.success(userDTO);
    }

    @Override
    public Resp checkUsers(Integer page, Integer size, String keyword, String order) {
        // 设置默认分页参数
        if (page == null || page <= 0) page = 1;
        if (size == null || size <= 0) size = 10;

        QueryWrapper<User> qw = new QueryWrapper<>();
        // 模糊查询 name 中包含关键字的条目
        qw.like("name", keyword);

        // 排序字段安全性检查
        if (order != null && !order.trim().isEmpty()) {
            String[] orderParts = order.split(",");
            String orderField = orderParts[0];
            String orderDirection = (orderParts.length > 1) ? orderParts[1] : "asc";

            // 验证排序字段是否在允许的字段范围内，防止 SQL 注入
            List<String> validOrderFields = Arrays.asList("create_time", "blog_cnt", "follower_cnt"); // 可根据实际业务修改
            if (!validOrderFields.contains(orderField)) {
                throw new BusinessException(Code.ERROR_PARAMS_INVALID);
            }

            // 设置排序方式
            if ("asc".equalsIgnoreCase(orderDirection)) {
                qw.orderByAsc(orderField);
            } else if ("desc".equalsIgnoreCase(orderDirection)) {
                qw.orderByDesc(orderField);
            } else {
                throw new BusinessException(Code.ERROR_PARAMS_INVALID); // 无效的排序方向
            }
        } else qw.orderByDesc("blog_cnt");

        Page<User> pageRequest = new Page<>(page, size);
        IPage<User> pageUsers = userMapper.selectPage(pageRequest, qw);
        return Resp.success(pageUsers);
    }

    @Override
    public Resp followUser(Integer followerId, Integer followedId) {
        // 检查 followedId 用户是否存在及合法
        User followedUser = userMapper.selectById(followedId);
        if (followedUser == null) throw new BusinessException(Code.ERROR_PARAMS_INVALID);
        if (followedUser.getStatus().equals(UserStatus.BANNED.getCode())) return Resp.error(Code.ERROR_PARAMS_INVALID, "the user you want to follow is banned");
        User followerUser = userMapper.selectById(followerId);

        // 查看是否已经建立follow关系
        QueryWrapper<Follow> qw = new QueryWrapper<>();
        qw.eq("follower_id", followerId);
        qw.eq("followed_id", followedId);
        Follow f = followMapper.selectOne(qw);
        if (f == null) {
            // 没有follow关系，创建新的follow关系
            f = new Follow();
            f.setFollowerId(followerId);
            f.setFollowedId(followedId);
            f.setIsValid((byte) 1);
            f.setCreateTime(LocalDateTime.now());
            followMapper.insert(f);
        } else {
            f.setIsValid((byte) 1);
            f.setCreateTime(LocalDateTime.now());
            followMapper.updateById(f);
        }

        // 修改 followed follower 的数据
        followedUser.setFollowerCnt(followedUser.getFollowerCnt() + 1);
        userMapper.updateById(followedUser);

        followerUser.setFollowedCnt(followerUser.getFollowedCnt() + 1);
        userMapper.updateById(followerUser);
        return Resp.success();
    }

    public Resp unfollowUser(Integer followerId, Integer followedId) {
        QueryWrapper<Follow> qw = new QueryWrapper<>();
        qw.eq("follower_id", followerId);
        qw.eq("followed_id", followedId);
        Follow f = followMapper.selectOne(qw);
        if (f == null) throw new BusinessException(Code.ERROR_PARAMS_INVALID);
        f.setIsValid((byte) 0);
        followMapper.updateById(f);

        User followedUser = userMapper.selectById(followedId);
        followedUser.setFollowerCnt(followedUser.getFollowerCnt() - 1);
        userMapper.updateById(followedUser);

        User followerUser = userMapper.selectById(followerId);
        followerUser.setFollowedCnt(followerUser.getFollowedCnt() - 1);
        userMapper.updateById(followerUser);
        return Resp.success();
    }

    @Override
    public Resp checkMyFollowerUsers(Integer myId) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        List<Integer> followerUserIds = followMapper.findFollowerIdsByUserId(myId);
        if (followerUserIds.isEmpty()) return Resp.success("no follower users", null);
        List<User> followerUsers = userMapper.selectBatchIds(followerUserIds);
        return Resp.success(followerUsers);
    }

    @Override
    public Resp checkMyFollowedUsers(Integer myId) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        List<Integer> followedUserIds = followMapper.findFollowedIdsByUserId(myId);
        if (followedUserIds.isEmpty()) return Resp.success("no followed users", null);
        List<User> followedUsers = userMapper.selectBatchIds(followedUserIds);
        return Resp.success(followedUsers);
    }
}
