//package com.ypy.blogbackend.service;
//
//import com.ypy.blogbackend.common.Code;
//import com.ypy.blogbackend.common.Description;
//import com.ypy.blogbackend.common.Resp;
//import com.ypy.blogbackend.common.exception.BusinessException;
//import com.ypy.blogbackend.dto.BlogDTO;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class UserBlogServiceTest {
//    @Resource
//    private UserBlogService userBlogService;
//
//    @Test
//    void sendBlog() {
//        BlogDTO dto = new BlogDTO();
//        Resp resp = null;
//        dto.setId(123);
//        dto.setAuthId(134);
//        dto.setAuthAccount("456");
//        dto.setAuthName("fshgh");
//        dto.setAuthAvatar("tyghj");
//        dto.setTitle("User01 的博客");
//        dto.setDescription("");
//        dto.setContent("今天是个好天气");
//        dto.setPermission((byte)1);
//        dto.setCreateTime(null);
//        dto.setUpdateTime(null);
//        resp = userBlogService.sendBlog(1, dto);
//        assertEquals(resp.getCode(), Code.SUCCESS.getCode());
//
//        dto.setId(1894);
//        dto.setAuthId(14434);
//        dto.setAuthAccount("4aaa6");
//        dto.setAuthName("fshgddh");
//        dto.setAuthAvatar("tyghj");
//        dto.setTitle("User02 的博客");
//        dto.setDescription("asdassd");
//        dto.setContent("有意思");
//        dto.setPermission((byte)2);
//        dto.setCreateTime(null);
//        dto.setUpdateTime(null);
//        resp = userBlogService.sendBlog(2, dto);
//        assertEquals(resp.getCode(), Code.SUCCESS.getCode());
//
//        dto.setId(1894);
//        dto.setAuthId(14434);
//        dto.setAuthAccount("4aaa6");
//        dto.setAuthName("fshgddh");
//        dto.setAuthAvatar("tyghj");
//        dto.setTitle("User02 的第二篇博客");
//        dto.setDescription("asdassd");
//        dto.setContent("有意思哈哈哈哈哈哈");
//        dto.setPermission((byte)2);
//        dto.setCreateTime(null);
//        dto.setUpdateTime(null);
//        resp = userBlogService.sendBlog(2, dto);
//        assertEquals(resp.getCode(), Code.SUCCESS.getCode());
//
//        dto.setId(1894);
//        dto.setAuthId(14434);
//        dto.setAuthAccount("4aaa6");
//        dto.setAuthName("fshgddh");
//        dto.setAuthAvatar("tyghj");
//        dto.setTitle("User03 的博客");
//        dto.setDescription("asdassd");
//        dto.setContent("有意思哈哈哈哈哈哈");
//        dto.setPermission((byte)2);
//        dto.setCreateTime(null);
//        dto.setUpdateTime(null);
//        resp = userBlogService.sendBlog(3, dto);
//        assertEquals(resp.getCode(), Code.SUCCESS.getCode());
//
//        dto.setId(1894);
//        dto.setAuthId(14434);
//        dto.setAuthAccount("4aaa6");
//        dto.setAuthName("fshgddh");
//        dto.setAuthAvatar("tyghj");
//        dto.setTitle("User03 的博客");
//        dto.setDescription("asdassd");
//        dto.setContent("有意思哈哈哈哈哈哈");
//        dto.setPermission((byte)2);
//        dto.setCreateTime(null);
//        dto.setUpdateTime(null);
//        BusinessException exception = assertThrows(BusinessException.class, () -> {
//            userBlogService.sendBlog(5, dto);
//        });
//        assertEquals(Code.ERROR_PARAMS_INVALID.getCode(), exception.getCode().getCode());
//    }
//
//    @Test
//    void deleteBlog() {
//        BusinessException exception = assertThrows(BusinessException.class, () -> {
//            userBlogService.deleteBlog(3, 2);
//        });
//
//        userBlogService.deleteBlog(3, 4);
//
//    }
//
//    @Test
//    void updateBlog() {
//    }
//
//    @Test
//    void checkBlog() {
//        Resp resp = null;
//        resp = userBlogService.checkBlog(1, 2);
//        System.out.println(resp.getData());
//        resp = userBlogService.checkBlog(2, null);
//        System.out.println(resp.getData());
//    }
//
//    @Test
//    void checkBlogs() {
//        Resp resp = null;
//        resp = userBlogService.checkBlogs(1, null, 1, 5, "哈", null);
//        System.out.println(resp.getData());
//    }
//}
