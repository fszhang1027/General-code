package com.sheng.heritagenexus.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sheng.heritagenexus.common.Constants;
import com.sheng.heritagenexus.common.enums.ResultCodeEnum;
import com.sheng.heritagenexus.common.enums.RoleEnum;
import com.sheng.heritagenexus.entity.Account;
import com.sheng.heritagenexus.exception.CustomException;
import com.sheng.heritagenexus.service.AdminService;
import com.sheng.heritagenexus.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张福生
 * @version 1.0
 * @data 2026/1/10 18:16
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从http请求标头里面拿到token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isNull(token)) {
            // 如果没拿到，那么再从请求参数里拿一次
            request.getParameter(Constants.TOKEN);
        }
        // 2. 开始执行认证
        if (ObjectUtil.isNull(token)) {
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            String userId = audience.split("-")[0];
            String role = audience.split("-")[1];
            // 根据用户角色判断用户属于哪个数据库表 然后查询用户数据
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            }
            if (RoleEnum.USER.name().equals(role)) {
                account = userService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        // 根据token里面携带的用户ID去对应的角色表查询  没查到--> 报了这个“用户不存在”错误
        if (ObjectUtil.isNull(account)) {
            // 用户不存在
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        try {
            // 通过用户的密码作为密钥再次验证 token 的合法性
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassWord())).build();
            // 验证 token
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            // 用户不存在
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        return true;
    }

}