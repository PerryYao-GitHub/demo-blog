package com.ypy.blogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ypy.blogbackend.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper extends BaseMapper<Follow>{

    @Select("SELECT followed_id FROM r_follow WHERE follower_id = #{userId} AND is_valid = 1")
    List<Integer> findFollowedIdsByUserId(@Param("userId") Integer userId);

    @Select("SELECT follower_id FROM r_follow WHERE followed_id = #{userId} AND is_valid = 1")
    List<Integer> findFollowerIdsByUserId(@Param("userId") Integer userId);
}
