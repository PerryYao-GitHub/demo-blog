package com.ypy.blogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ypy.blogbackend.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("SELECT auth_id FROM t_blog WHERE id = #{blog_id}")
    Integer getAuthId(@Param("blog_id") Integer blogId);
}
