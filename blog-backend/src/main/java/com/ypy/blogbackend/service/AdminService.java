package com.ypy.blogbackend.service;

import com.ypy.blogbackend.common.Resp;

public interface AdminService {
    Resp switchUserStatus(Integer userId);
}
