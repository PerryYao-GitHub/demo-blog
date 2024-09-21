package com.ypy.blogbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ypy.blogbackend.common.BlogPermission;
import com.ypy.blogbackend.common.Code;
import com.ypy.blogbackend.common.Description;
import com.ypy.blogbackend.common.Resp;
import com.ypy.blogbackend.common.exception.BusinessException;
import com.ypy.blogbackend.dto.BlogDTO;
import com.ypy.blogbackend.entity.Blog;
import com.ypy.blogbackend.entity.User;
import com.ypy.blogbackend.mapper.BlogMapper;
import com.ypy.blogbackend.mapper.UserMapper;
import com.ypy.blogbackend.service.UserBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserBlogServiceImpl implements UserBlogService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    BlogMapper blogMapper;

    private void dealBlogDTO(Blog blog, BlogDTO blogDTO) {
        if (StrUtil.isBlank(blogDTO.getTitle())) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (StrUtil.isBlank(blogDTO.getContent())) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (StrUtil.isBlank(blogDTO.getDescription())) {
            blogDTO.setDescription(blogDTO.getContent().substring(0, Math.min(blogDTO.getContent().length(), 20)) + "...");
        }
        if (blogDTO.getPermission() == null || !BlogPermission.isValid(blogDTO.getPermission())) {
            blogDTO.setPermission(BlogPermission.SYS_USERS.getCode());
        }
        String blogTitle = blogDTO.getTitle();
        String blogContent = blogDTO.getContent();
        String blogDescription = blogDTO.getDescription();
        Byte blogPermission = blogDTO.getPermission();

        blog.setTitle(blogTitle);
        blog.setContent(blogContent);
        blog.setDescription(blogDescription);
        blog.setPermission(blogPermission);
    }

    @Override
    public Resp sendBlog(Integer userId, BlogDTO dto) {
        if (userId == null || dto == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        // 校验
        Blog blog = new Blog();
        this.dealBlogDTO(blog, dto);
        // 查用户 更新博客数
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(Code.ERROR_PARAMS_INVALID);
        user.setBlogCnt(user.getBlogCnt() + 1);
        String authAccount = user.getAccount();
        userMapper.updateById(user);
        // 增加博客
        blog.setAuthId(userId);
        blog.setAuthAccount(authAccount);
        blog.setCreateTime(LocalDateTime.now());
        blog.setUpdateTime(LocalDateTime.now());
        blogMapper.insert(blog);
        return Resp.success();
    }

    @Override
    public Resp deleteBlog(Integer userId, Integer blogId) {
        if (userId == null || blogId == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        // 校验
        if (!userId.equals(blogMapper.getAuthId(blogId))) throw new BusinessException(Code.ERROR_AUTH);
        blogMapper.deleteById(blogId);
        User user = userMapper.selectById(userId);
        user.setBlogCnt(user.getBlogCnt() - 1);  // 用户博客数减一
        userMapper.updateById(user);
        return Resp.success();
    }

    @Override
    public Resp updateBlog(Integer userId, BlogDTO dto) {
        if (userId == null || dto == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        // 校验
        Blog blog = blogMapper.selectById(dto.getId());
        if (!Objects.equals(blog.getAuthId(), userId)) throw new BusinessException(Code.ERROR_AUTH);
        // 更新博客
        this.dealBlogDTO(blog, dto);
        blog.setUpdateTime(LocalDateTime.now());
        blogMapper.updateById(blog);
        return Resp.success();
    }

    @Override
    public Resp checkBlog(Integer blogId, Integer visitorId) {
        if (blogId == null) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        Blog blog = blogMapper.selectById(blogId);
        Integer authId = blog.getAuthId();
        Byte blogPermission = blog.getPermission();

//        // 对身份分类
//        if (visitorId == null) {  // 游客
//            if (!Objects.equals(blogPermission, BlogPermission.EVERYONE.getCode())) throw new BusinessException(Code.ERROR_AUTH, Description.NO_BLOG_PERMISSION.getStr());
//        } else if (!visitorId.equals(authId)) {  // 系统用户
//            if (Objects.equals(blogPermission, BlogPermission.ONLY_ME.getCode())) throw new BusinessException(Code.ERROR_AUTH, Description.NO_BLOG_PERMISSION.getStr());
//        } // else 自己看自己

        User author = userMapper.selectById(blog.getAuthId());
        BlogDTO blogDTO = BlogDTO.toDTO(blog);
        blogDTO.setAuthName(author.getName());
        blogDTO.setAuthAvatar(author.getAvatar());
        return Resp.success(blogDTO);
    }

    @Override
    public Resp checkBlogs(Integer visitorId, Integer authId, Integer page, Integer size, String keyword, String order) {
        // 设置默认分页参数
        if (page == null || page <= 0) page = 1;
        if (size == null || size <= 0) size = 10;

        QueryWrapper<Blog> qw = new QueryWrapper<>();

        // 查询该用户发布的博客
        if (authId != null) qw.eq("auth_id", authId);
//        // 根据用户身份决定博客的权限条件
//        if (visitorId == null) {  // 游客
//            qw.eq("permission", BlogPermission.EVERYONE.getCode());
//        } else if (!visitorId.equals(authId)) {  // 用户 但不是自己
//            qw.in("permission", BlogPermission.EVERYONE.getCode(), BlogPermission.SYS_USERS.getCode());
//        } // else 自己


        // 如果关键字不为空，则进行模糊查询
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 模糊查询 title、description 和 content 中包含关键字的条目
            qw.and(wrapper -> wrapper
                    .like("title", keyword)
                    .or()
                    .like("description", keyword)
                    .or()
                    .like("content", keyword)
            );
        }

        // 排序字段安全性检查
        if (order != null && !order.trim().isEmpty()) {
            String[] orderParts = order.split(",");
            String orderField = orderParts[0];
            String orderDirection = (orderParts.length > 1) ? orderParts[1] : "asc";

            // 验证排序字段是否在允许的字段范围内，防止 SQL 注入
            List<String> validOrderFields = Arrays.asList("create_time", "update_time", "title"); // 可根据实际业务修改
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
        } else qw.orderByDesc("create_time");

        // 创建分页对象
        Page<Blog> pageRequest = new Page<>(page, size);

        // 执行分页查询
        IPage<Blog> pageBlogs = blogMapper.selectPage(pageRequest, qw);

        // 封装响应并返回
        return Resp.success(pageBlogs);
    }
}
