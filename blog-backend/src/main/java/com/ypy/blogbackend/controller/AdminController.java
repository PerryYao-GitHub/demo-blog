package com.ypy.blogbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.ypy.blogbackend.common.Code;
import com.ypy.blogbackend.common.Resp;
import com.ypy.blogbackend.common.UserRole;
import com.ypy.blogbackend.common.exception.BusinessException;
import com.ypy.blogbackend.dto.AccountPasswordDTO;
import com.ypy.blogbackend.service.AdminService;
import com.ypy.blogbackend.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    // 管理员登录
    @PostMapping("/login")
    public Resp login(@RequestBody AccountPasswordDTO dto) {
        String account = dto.getAccount();
        String password = dto.getPassword();
        if (StrUtil.isBlank(account) || StrUtil.isBlank(password)) throw new BusinessException(Code.ERROR_PARAMS_NULL);
        if (!(account.equals("admin") && password.equals("123456"))) throw new BusinessException(Code.ERROR_PARAMS_INVALID, "wrong username or password");
        String token = JwtUtils.genToken(0, UserRole.ADMIN.getCode());
        return Resp.success(token);
    }

    // 禁评某用户
    @GetMapping("/user/switch_status/{id}")
    public Resp switchUserStatus(@PathVariable Integer id) {
        if (id == null || id <= 0) return Resp.error(Code.ERROR_PARAMS_INVALID);
        return adminService.switchUserStatus(id);
    }
}
