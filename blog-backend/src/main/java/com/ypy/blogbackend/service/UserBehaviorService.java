package com.ypy.blogbackend.service;

import com.ypy.blogbackend.common.Resp;

public interface UserBehaviorService {
    Resp checkOneUser(Integer visitorId, Integer targetId);
    Resp checkUsers(Integer page, Integer size, String keyword, String order);
    Resp followUser(Integer followerId, Integer followedId);
    Resp unfollowUser(Integer followerId, Integer followedId);

    Resp checkMyFollowerUsers(Integer myId);

    Resp checkMyFollowedUsers(Integer myId);
}
