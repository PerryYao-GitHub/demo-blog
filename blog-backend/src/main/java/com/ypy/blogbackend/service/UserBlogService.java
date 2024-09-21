package com.ypy.blogbackend.service;

import com.ypy.blogbackend.common.Resp;
import com.ypy.blogbackend.dto.BlogDTO;

public interface UserBlogService {
    Resp sendBlog(Integer userId, BlogDTO dto);
    Resp deleteBlog(Integer userId, Integer blogId);
    Resp updateBlog(Integer userId, BlogDTO dto);


    /**
     *
     * @param blogId
     * @param visitorId
     * @return
     */
    Resp checkBlog(Integer blogId, Integer visitorId);

    /**
     *
     * @param visitorId >> null: 访客, not null: SYS_USER
     * @param authId >> null: 查看全部 (广场), not null: 查看某一个, not null && == visitorId
     * @param page
     * @param size
     * @param keyword
     * @param order
     * @return
     */
    Resp checkBlogs(Integer visitorId, Integer authId, Integer page, Integer size, String keyword, String order);
}
