package com.sheng.heritagenexus.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sheng.heritagenexus.common.Constants;
import com.sheng.heritagenexus.common.enums.RoleEnum;
import com.sheng.heritagenexus.entity.Account;
import com.sheng.heritagenexus.service.AdminService;
import com.sheng.heritagenexus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
/**
 * @author 张福生
 * @version 1.0
 * @data 2026/1/10 18:24
 */
@Component
public class TokenUtils {
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "userServiceImpl")
    private UserService userService;

    private static AdminService staticAdminService;
    private static UserService staticUserService;

    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticUserService = userService;
    }
    /**
     * 生成 Token
     */
    public static String createToken(String data, String sign) {
        // audience是存储数据的一个媒介  存储用户ID和用户的角色  1-ADMIN
        return JWT.create().withAudience(data)
                // 设置过期时间1天后
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1))
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录的用户
     */
    public static Account getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(Constants.TOKEN);
            String audience = JWT.decode(token).getAudience().get(0);
            String[] userRole = audience.split("-");
            Integer userId = Integer.valueOf(userRole[0]);
            String role = userRole[1];
            if (RoleEnum.ADMIN.name().equals(role)) {
                return staticAdminService.selectById(userId);
            }
            if (RoleEnum.USER.name().equals(role)) {
                return staticUserService.selectById(userId);
            }
        } catch (Exception e) {
            log.error("获取当前登录用户出错", e);
        }
        return null;
    }

}