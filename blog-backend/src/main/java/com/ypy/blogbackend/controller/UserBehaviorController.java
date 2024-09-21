package com.ypy.blogbackend.controller;

import com.ypy.blogbackend.common.Resp;
import com.ypy.blogbackend.service.UserBehaviorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserBehaviorController {
    @Resource
    private UserBehaviorService userBehaviorService;

    @GetMapping("/check/one/user")
    public Resp checkOneUser(HttpServletRequest request, @RequestParam Integer targetId) {
        Integer visitorId = UserAccountController.getUserIdFromRequest(request);
        return userBehaviorService.checkOneUser(visitorId, targetId);
    }

    @GetMapping("/check/users")
    public Resp checkUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String order
    ) {
        return userBehaviorService.checkUsers(page, size, keyword, order);
    }

    @GetMapping("/follow/user")
    public Resp followUser(HttpServletRequest request, @RequestParam Integer targetId) {
        Integer followerId = UserAccountController.getUserIdFromRequest(request);
        return userBehaviorService.followUser(followerId, targetId);
    }

    @GetMapping("/unfollow/user")
    public Resp unfollowUser(HttpServletRequest request, @RequestParam Integer targetId) {
        Integer followerId = UserAccountController.getUserIdFromRequest(request);
        return userBehaviorService.unfollowUser(followerId, targetId);
    }

    @GetMapping("/check/my/follower/users")
    public Resp checkMyFollowerUsers(HttpServletRequest request) {
        Integer myId = UserAccountController.getUserIdFromRequest(request);
        return userBehaviorService.checkMyFollowerUsers(myId);
    }

    @GetMapping("/check/my/followed/users")
    public Resp checkMyFollowedUsers(HttpServletRequest request) {
        Integer myId = UserAccountController.getUserIdFromRequest(request);
        return userBehaviorService.checkMyFollowedUsers(myId);
    }
}
