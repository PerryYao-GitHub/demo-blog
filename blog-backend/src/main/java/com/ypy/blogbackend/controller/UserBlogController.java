package com.ypy.blogbackend.controller;

import com.ypy.blogbackend.common.Resp;
import com.ypy.blogbackend.dto.BlogDTO;
import com.ypy.blogbackend.service.UserBlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserBlogController {
    @Resource
    private UserBlogService userBlogService;

    @PostMapping("/send/blog")
    public Resp sendBlog(@RequestBody BlogDTO blogDTO, HttpServletRequest request) {
        Integer userId = UserAccountController.getUserIdFromRequest(request);
        return userBlogService.sendBlog(userId, blogDTO);
    }

    @GetMapping("/delete/blog")
    public Resp deleteBlog(@RequestParam Integer blogId, HttpServletRequest request) {
        Integer userId = UserAccountController.getUserIdFromRequest(request);
        return userBlogService.deleteBlog(userId, blogId);
    }

    @PostMapping("/update/blog")
    public Resp updateBlog(@RequestBody BlogDTO blogDTO, HttpServletRequest request) {
        Integer userId = UserAccountController.getUserIdFromRequest(request);
        return userBlogService.updateBlog(userId, blogDTO);
    }

    @GetMapping("/check/blog")
    public Resp checkBlog(@RequestParam Integer blogId, HttpServletRequest request) {
        Integer visitorId = UserAccountController.getUserIdFromRequest(request);
        return userBlogService.checkBlog(blogId, visitorId);
    }

    @GetMapping("/check/blogs")
    public Resp checkBlogs(
            HttpServletRequest request,
            @RequestParam(required = false) Integer authId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String order) {
        Integer visitorId = UserAccountController.getUserIdFromRequest(request);
        return userBlogService.checkBlogs(visitorId, authId, page, size, keyword, order);
    }
}
